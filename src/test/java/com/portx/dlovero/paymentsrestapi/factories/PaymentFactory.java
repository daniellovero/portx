package com.portx.dlovero.paymentsrestapi.factories;

import com.portx.dlovero.paymentsrestapi.domain.model.*;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentFactory {
    public Customer createValidCustomer(long id, String name) {
        return new Customer(id, name);
    }

    public AmountPaid createValidAmountPaid(int amount, String currency) {
        return new AmountPaid(new BigDecimal(amount), currency);
    }

    public Account createValidAccount(long accountNumber, String accountType) {
        return new Account(accountNumber, accountType);
    }

    public Payment createValidPayment() {
        Customer originator = createValidCustomer(1L, "originator");
        Customer beneficiary = createValidCustomer(2L, "beneficiary");
        AmountPaid amountPaid = createValidAmountPaid(10, "USD");
        Account senderAccount = createValidAccount(1L, "CA");
        Account receiverAccount = createValidAccount(2L, "CA");
        return new Payment(1L, amountPaid, originator, beneficiary, senderAccount, receiverAccount, PAYMENT_STATUS.CREATED);
    }

    public Payment createInvalidPayment() {
        Customer originator = createValidCustomer(0L, "originator");
        Customer beneficiary = createValidCustomer(2L, "beneficiary");
        AmountPaid amountPaid = createValidAmountPaid(10, "USD");
        Account senderAccount = createValidAccount(1L, "CA");
        Account receiverAccount = createValidAccount(2L, "CA");
        return new Payment(1L, amountPaid, originator, beneficiary, senderAccount, receiverAccount, PAYMENT_STATUS.CREATED);
    }

    public UUID getValidIdempotentKey() {
        return UUID.randomUUID();
    }

    public UUID createInvalidIdempotentKey() {
        return UUID.fromString("07d02584-cd94-11ed-afa1-0242ac120002");
    }
}
