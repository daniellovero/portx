package com.portx.dlovero.paymentsrestapi.domain.model;

import java.math.BigDecimal;

public class AmountPaid {
    private final BigDecimal amount;
    // This can be a set of values defined by extension instead of string.
    private final String currency;

    public AmountPaid(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
