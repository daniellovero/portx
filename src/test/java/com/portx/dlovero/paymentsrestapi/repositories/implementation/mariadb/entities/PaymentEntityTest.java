package com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.entities;

import com.portx.dlovero.paymentsrestapi.domain.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PaymentEntityTest {

    @Test
    void testToModelContractWorks() {
        Customer originator = new Customer(1L, "originator");
        Customer beneficiary = new Customer(2L, "beneficiary");
        AmountPaid amountPaid = new AmountPaid(new BigDecimal(10), "USD");
        Account senderAccount = new Account(1L, "CA");
        Account receiverAccount = new Account(2L, "CA");
        UUID idempotentKey = UUID.fromString("9864c7b1-b99b-48db-a643-428bf8c090da");
        Payment originalPayment = new Payment(1L, amountPaid, originator, beneficiary, senderAccount, receiverAccount, PAYMENT_STATUS.SENT);
        PaymentEntity paymentEntity = new PaymentEntity(originalPayment, idempotentKey);

        Payment mappedPayment = paymentEntity.toModel();

        // All fields are equal but id and status
        assertThat(mappedPayment.getAmountPaid()).isEqualToComparingFieldByFieldRecursively(originalPayment.getAmountPaid());
        assertThat(mappedPayment.getBeneficiary()).isEqualToComparingFieldByFieldRecursively(originalPayment.getBeneficiary());
        assertThat(mappedPayment.getReceiver()).isEqualToComparingFieldByFieldRecursively(originalPayment.getReceiver());
        assertThat(mappedPayment.getOriginator()).isEqualToComparingFieldByFieldRecursively(originalPayment.getOriginator());
        assertThat(mappedPayment.getSender()).isEqualToComparingFieldByFieldRecursively(originalPayment.getSender());

        // Because it was not saved into the DB and is a readonly field
        assertThat(mappedPayment.getId()).isEqualTo(null);

        // Every created entity's status is created
        assertThat(mappedPayment.getStatus()).isEqualTo(PAYMENT_STATUS.CREATED);
    }
}
