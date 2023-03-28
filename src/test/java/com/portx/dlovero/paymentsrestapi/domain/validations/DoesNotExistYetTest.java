package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomPaymentAlreadyExists;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import com.portx.dlovero.paymentsrestapi.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class DoesNotExistYetTest {

    private Payment validPayment;
    private UUID validIdempotentKey;

    @BeforeEach
    void setUp() {
        PaymentFactory paymentFactory = new PaymentFactory();
        validPayment = paymentFactory.createValidPayment();
        validIdempotentKey = paymentFactory.getValidIdempotentKey();
    }

    @Test
    void testThatIfPaymentExistItThrows() {
        PaymentService mockedPaymentService = mock(PaymentService.class);
        Mockito.when(mockedPaymentService.checkIfPaymentAlreadyExists(validIdempotentKey)).thenReturn(true);

        Payment payment = new PaymentFactory().createValidPayment();
        CustomPaymentAlreadyExists thrownException = assertThrows(CustomPaymentAlreadyExists.class, () -> new DoesNotExistYet(mockedPaymentService).validate(payment, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Payment already exists");
    }

    @Test
    void testThatIfPaymentDoesNotExistItDoesNotThrow() {
        PaymentService mockedPaymentService = mock(PaymentService.class);
        Mockito.when(mockedPaymentService.checkIfPaymentAlreadyExists(validIdempotentKey)).thenReturn(false);

        assertDoesNotThrow(() -> new DoesNotExistYet(mockedPaymentService).validate(validPayment, validIdempotentKey));
    }
}
