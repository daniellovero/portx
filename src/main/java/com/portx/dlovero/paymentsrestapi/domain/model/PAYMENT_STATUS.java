package com.portx.dlovero.paymentsrestapi.domain.model;

public enum PAYMENT_STATUS {
    CREATED("CREATED"),
    SENT("SENT"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String status;

    PAYMENT_STATUS(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
