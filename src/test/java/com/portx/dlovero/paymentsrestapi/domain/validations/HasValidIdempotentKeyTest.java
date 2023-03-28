package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomInvalidUUIDVersion;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HasValidIdempotentKeyTest {

    private Payment validPayment;
    private PaymentFactory paymentFactory;

    @BeforeEach
    void setUp() {
        paymentFactory = new PaymentFactory();
        validPayment = paymentFactory.createValidPayment();
    }

    @Test
    void testThatInvalidIdempotentThrows() {
        CustomInvalidUUIDVersion thrownException = assertThrows(CustomInvalidUUIDVersion.class, () -> new HasValidIdempotentKey().validate(validPayment, paymentFactory.createInvalidIdempotentKey()));
        assertThat(thrownException.getMessage()).isEqualTo("Invalid UUID version. UUID version is 4 and variant 2");
    }

    @Test
    void testThatValidIdempotentKeyDoesNotThrow() {
        assertDoesNotThrow(() -> new HasValidIdempotentKey().validate(validPayment, paymentFactory.getValidIdempotentKey()));
    }
}
