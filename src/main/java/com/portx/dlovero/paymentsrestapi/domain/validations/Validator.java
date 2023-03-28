package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.services.PaymentService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Validator {

    private List<Validation> validations;

    public Validator(PaymentService paymentService) {
        validations = new ArrayList<>();
        validations.add(new HasValidIdempotentKey());
        validations.add(new DoesNotExistYet(paymentService));
        validations.add(new HasValidDomainValues());
    }

    public void assertValidParameters(Payment payment, UUID headers) {
        validations.forEach(validation -> validation.validate(payment, headers));
    }
}
