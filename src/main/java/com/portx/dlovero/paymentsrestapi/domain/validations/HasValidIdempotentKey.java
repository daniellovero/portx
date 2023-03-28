package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomInvalidUUIDVersion;
import com.portx.dlovero.paymentsrestapi.i18n.Messages;

import java.util.UUID;

public class HasValidIdempotentKey implements Validation {
    @Override
    public void validate(Payment payment, UUID idempotentKey) {
        if (idempotentKey.variant() != 2 || idempotentKey.version() != 4) {
            throw new CustomInvalidUUIDVersion(Messages.Exceptions.INVALID_UUID_VERSION);
        }
    }
}
