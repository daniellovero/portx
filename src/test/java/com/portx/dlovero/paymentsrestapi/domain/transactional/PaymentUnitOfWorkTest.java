package com.portx.dlovero.paymentsrestapi.domain.transactional;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.queue.PaymentProducer;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import com.portx.dlovero.paymentsrestapi.repositories.RelationalDatabasePaymentRepository;
import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.MariaDBPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DataJpaTest
class PaymentUnitOfWorkTest {
    private PaymentProducer paymentProducer;
    @SpyBean
    private RelationalDatabasePaymentRepository paymentRepository;
    @Autowired
    private MariaDBPaymentRepository dbPaymentRepository;
    private Payment validPayment;
    private UUID validIdempotentKey;

    @BeforeEach
    void setUp() {
        paymentProducer = mock(PaymentProducer.class);
        PaymentFactory paymentFactory = new PaymentFactory();
        validPayment = paymentFactory.createValidPayment();
        validIdempotentKey = paymentFactory.getValidIdempotentKey();
    }

    @Test
    void testThatIfRepositoryFailsToSaveItDoesNotPostToQueue() {
        PaymentUnitOfWork unitOfWork = new PaymentUnitOfWork(paymentProducer, paymentRepository);

        doThrow(new RuntimeException("DB Fail")).when(paymentRepository).save(validPayment, validIdempotentKey);
        doNothing().when(paymentProducer).sendPayment(validPayment);

        /*
         * This is focused specially on the unified behavior of `doInTransaction` method.
         * So even if the logic may vary based on how it's used, that's not the purpose of the test.
         * That's why I chose to test it as white-box here.
         */
        RuntimeException exceptionThrown = assertThrows(RuntimeException.class, () -> unitOfWork.doInTransaction(() -> {
            unitOfWork.save(validPayment, validIdempotentKey);
            unitOfWork.send(validPayment);
        }));

        verify(paymentRepository).save(validPayment, validIdempotentKey);
        verify(paymentProducer, times(0)).sendPayment(validPayment);
        assertThat(exceptionThrown.getMessage()).isEqualTo("DB Fail");
        assertThat(dbPaymentRepository.findByIdempotentKey(validIdempotentKey).isEmpty()).isTrue();
    }

    @Test
    void testThatIfEverythingWorksItDoesComplete() {
        PaymentUnitOfWork unitOfWork = new PaymentUnitOfWork(paymentProducer, paymentRepository);

        doCallRealMethod().when(paymentRepository).save(validPayment, validIdempotentKey);
        doNothing().when(paymentProducer).sendPayment(validPayment);

        /*
         * And here as well.
         */
        unitOfWork.doInTransaction(() -> {
            unitOfWork.save(validPayment, validIdempotentKey);
            unitOfWork.send(validPayment);
        });

        verify(paymentRepository).save(validPayment, validIdempotentKey);
        verify(paymentProducer).sendPayment(validPayment);
        assertThat(dbPaymentRepository.findByIdempotentKey(validIdempotentKey).isEmpty()).isFalse();
    }

}