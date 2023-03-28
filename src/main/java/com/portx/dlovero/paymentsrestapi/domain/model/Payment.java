package com.portx.dlovero.paymentsrestapi.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    AmountPaid amountPaid;
    Customer originator;
    Customer beneficiary;
    Account sender;
    Account receiver;
    PAYMENT_STATUS status;

    public Payment(Long id, AmountPaid amountPaid, Customer originator, Customer beneficiary, Account sender, Account receiver, PAYMENT_STATUS status) {
        this.id = id;
        this.amountPaid = amountPaid;
        this.originator = originator;
        this.beneficiary = beneficiary;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public AmountPaid getAmountPaid() {
        return amountPaid;
    }

    public Customer getOriginator() {
        return originator;
    }

    public Customer getBeneficiary() {
        return beneficiary;
    }

    public Account getSender() {
        return sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public PAYMENT_STATUS getStatus() {
        return status;
    }
}
