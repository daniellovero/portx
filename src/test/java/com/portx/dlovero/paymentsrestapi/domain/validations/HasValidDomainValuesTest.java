package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.*;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomInvalidField;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HasValidDomainValuesTest {
    private Customer validOriginator;
    private Customer validBeneficiary;
    private AmountPaid validAmountPaid;
    private Account validSenderAccount;
    private Account validReceiverAccount;
    private UUID validIdempotentKey;


    @BeforeEach
    public void setUp() {
        PaymentFactory factory = new PaymentFactory();
        // All valid values
        validOriginator = factory.createValidCustomer(1L, "originator");
        validBeneficiary = factory.createValidCustomer(2L, "beneficiary");
        validAmountPaid = factory.createValidAmountPaid(10, "USD");
        validSenderAccount = factory.createValidAccount(1L, "CA");
        validReceiverAccount = factory.createValidAccount(2L, "CA");
        validIdempotentKey = UUID.fromString("9864c7b1-b99b-48db-a643-428bf8c090da");
    }

    @Test
    void testThatIfAmountPaidIsLessThanZeroItFails() {
        AmountPaid invalidAmountPaid = new AmountPaid(new BigDecimal(-1), "USD");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, invalidAmountPaid, validOriginator, validBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field amount paid cannot be less than one");
    }

    @Test
    void testThatIfAmountCurrencyIsEmptyItFails() {
        AmountPaid invalidAmountPaid = new AmountPaid(new BigDecimal(1), "");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, invalidAmountPaid, validOriginator, validBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field amount currency cannot be blank");
    }

    @Test
    void testThatIfBeneficiaryCustomerIdIsZeroOrLessItFails() {
        Customer invalidBeneficiary = new Customer(0L, "beneficiary");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, validOriginator, invalidBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field beneficiary id cannot be less than one");
    }

    @Test
    void testThatIfBeneficiaryCustomerNameIsBlankItFails() {
        Customer invalidBeneficiary = new Customer(1L, "");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, validOriginator, invalidBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field beneficiary name cannot be blank");
    }

    @Test
    void testThatIfOriginatorCustomerIdIsZeroOrLessItFails() {
        Customer invalidOriginator = new Customer(0L, "originator");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, invalidOriginator, validBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field originator id cannot be less than one");
    }

    @Test
    void testThatIfOriginatorCustomerNameIsBlankItFails() {
        Customer invalidOriginator = new Customer(1L, "");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, invalidOriginator, validBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field originator name cannot be blank");
    }

    @Test
    void testThatIfReceiverAccountNumberIsZeroOrLessItFails() {
        Account invalidReceiver = new Account(0L, "CA");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, validOriginator, validBeneficiary, validSenderAccount, invalidReceiver, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field receiver account number cannot be less than one");
    }

    @Test
    void testThatIfReceiverAccountTypeIsBlankItFails() {
        Account invalidReceiver = new Account(1L, "");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, validOriginator, validBeneficiary, validSenderAccount, invalidReceiver, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field receiver account type cannot be blank");
    }

    @Test
    void testThatIfSenderAccountNumberIsZeroOrLessItFails() {
        Account invalidSender = new Account(0L, "CA");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, validOriginator, validBeneficiary, invalidSender, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field sender account number cannot be less than one");
    }

    @Test
    void testThatIfSenderAccountTypeIsBlankItFails() {
        Account invalidSender = new Account(1L, "");
        Payment paymentWithInvalidAmountPaid = new Payment(1L, validAmountPaid, validOriginator, validBeneficiary, invalidSender, validReceiverAccount, PAYMENT_STATUS.SENT);

        CustomInvalidField thrownException = assertThrows(CustomInvalidField.class, () -> new HasValidDomainValues().validate(paymentWithInvalidAmountPaid, validIdempotentKey));
        assertThat(thrownException.getMessage()).isEqualTo("Field sender account type cannot be blank");
    }

    @Test
    void testThatValidPaymentDoesNotThrowError() {
        Payment payment = new Payment(1L, validAmountPaid, validOriginator, validBeneficiary, validSenderAccount, validReceiverAccount, PAYMENT_STATUS.SENT);

        assertDoesNotThrow(() -> new HasValidDomainValues().validate(payment, validIdempotentKey));
    }
}
