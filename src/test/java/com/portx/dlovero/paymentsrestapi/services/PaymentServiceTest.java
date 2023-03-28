package com.portx.dlovero.paymentsrestapi.services;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.queue.PaymentProducer;
import com.portx.dlovero.paymentsrestapi.domain.transactional.PaymentUnitOfWork;
import com.portx.dlovero.paymentsrestapi.domain.transactional.UnitOfWork;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomPaymentAlreadyExists;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import com.portx.dlovero.paymentsrestapi.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentService paymentService;
    private PaymentFactory paymentFactory;
    private PaymentProducer paymentProducer;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentProducer = mock(PaymentProducer.class);
        UnitOfWork paymentUnitOfWork = new PaymentUnitOfWork(paymentProducer, paymentRepository);
        paymentService = new PaymentService(paymentUnitOfWork, paymentRepository);
        paymentFactory = new PaymentFactory();
    }

    @Test
    void shouldReturnPaymentIfDoesNotExistsInDb() {
        when(paymentRepository.getById(anyLong())).thenReturn(Optional.of(paymentFactory.createValidPayment()));

        Optional<Payment> returnedPayment = paymentService.getPaymentById(1L);

        verify(paymentRepository).getById(1L);
        assertThat(returnedPayment).isPresent();
    }

    @Test
    void shouldNotReturnPaymentIfItDoesNotExistsYet() {
        when(paymentRepository.getById(anyLong())).thenReturn(Optional.empty());

        Optional<Payment> returnedPayment = paymentService.getPaymentById(1L);

        verify(paymentRepository).getById(1L);
        assertThat(returnedPayment).isNotPresent();
    }

    @Test
    void shouldCreatePaymentIfItsValidAndDoesNotExistsYet() {
        when(paymentRepository.save(any(Payment.class), any(UUID.class))).thenReturn(paymentFactory.createValidPayment());
        when(paymentRepository.paymentAlreadyExists(any(UUID.class))).thenReturn(false);

        UUID validUUID = paymentFactory.getValidIdempotentKey();
        Payment validPayment = paymentFactory.createValidPayment();

        Optional<Payment> savedPayment = paymentService.saveAndPublishPayment(validPayment, validUUID);

        verify(paymentRepository).paymentAlreadyExists(validUUID);
        verify(paymentRepository).save(validPayment, validUUID);
        verify(paymentProducer).sendPayment(savedPayment.get());

        assertThat(savedPayment).isPresent();
    }

    @Test
    void shouldNotCreatePaymentWhenIdempotentKeyIsFoundInDB() {
        UUID validIdempotentKey = paymentFactory.getValidIdempotentKey();
        Payment validPayment = paymentFactory.createValidPayment();
        when(paymentRepository.paymentAlreadyExists(validIdempotentKey)).thenReturn(true);

        CustomPaymentAlreadyExists exceptionThrown = assertThrows(CustomPaymentAlreadyExists.class, () -> {
            paymentService.saveAndPublishPayment(validPayment, validIdempotentKey);
        });

        verify(paymentRepository).paymentAlreadyExists(validIdempotentKey);
        verify(paymentRepository, times(0)).save(validPayment, validIdempotentKey);
        verify(paymentProducer, times(0)).sendPayment(validPayment);
        assertThat(exceptionThrown.getMessage()).isEqualTo("Payment already exists");
    }

    @Test
    void shouldNotPublishMessageOfCreatedPaymentIfDatabaseFails() {
        UUID validIdempotentKey = paymentFactory.getValidIdempotentKey();
        Payment validPayment = paymentFactory.createValidPayment();
        when(paymentRepository.save(validPayment, validIdempotentKey)).thenThrow(new RuntimeException("Fail!"));

        RuntimeException exceptionThrown = assertThrows(RuntimeException.class, () -> {
            paymentService.saveAndPublishPayment(validPayment, validIdempotentKey);
        });

        verify(paymentRepository).paymentAlreadyExists(validIdempotentKey);
        verify(paymentRepository).save(validPayment, validIdempotentKey);
        verify(paymentProducer, times(0)).sendPayment(validPayment);
        assertThat(exceptionThrown.getMessage()).isEqualTo("Fail!");
    }

    @Test
    void shouldNotPublishMessageOfCreatedPaymentIfDatabaseReturnsNull() {
        UUID validIdempotentKey = paymentFactory.getValidIdempotentKey();
        Payment validPayment = paymentFactory.createValidPayment();
        when(paymentRepository.save(validPayment, validIdempotentKey)).thenReturn(null);

        RuntimeException exceptionThrown = assertThrows(RuntimeException.class, () -> {
            paymentService.saveAndPublishPayment(validPayment, validIdempotentKey);
        });

        verify(paymentRepository).paymentAlreadyExists(validIdempotentKey);
        verify(paymentRepository).save(validPayment, validIdempotentKey);
        verify(paymentProducer, times(0)).sendPayment(validPayment);
        assertThat(exceptionThrown.getMessage()).isEqualTo("Something went wrong while saving payment!");
    }

}
