package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomPaymentAlreadyExists;
import com.portx.dlovero.paymentsrestapi.i18n.Messages;
import com.portx.dlovero.paymentsrestapi.services.PaymentService;

import java.util.UUID;


public class DoesNotExistYet implements Validation {
    private final PaymentService paymentService;

    public DoesNotExistYet(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void validate(Payment payment, UUID idempotentKey) {
        // Double dispatch
        if (paymentService.checkIfPaymentAlreadyExists(idempotentKey)) {
            throw new CustomPaymentAlreadyExists(Messages.Exceptions.PAYMENT_ALREADY_EXISTS);
        }
    }
}
