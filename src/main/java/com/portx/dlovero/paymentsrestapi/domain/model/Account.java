package com.portx.dlovero.paymentsrestapi.domain.model;

public class Account {
    // To facilitate sorting or comparison
    private final Long accountNumber;
    private final String accountType;

    public Account(Long accountNumber, String accountType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
