package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;

import java.util.UUID;

public interface Validation {
    void validate(Payment payment, UUID idempotentKey);
}
