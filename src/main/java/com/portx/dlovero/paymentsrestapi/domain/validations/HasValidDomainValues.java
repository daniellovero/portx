package com.portx.dlovero.paymentsrestapi.domain.validations;

import com.portx.dlovero.paymentsrestapi.domain.model.PAYMENT_STATUS;
import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.validations.exceptions.CustomInvalidField;
import com.portx.dlovero.paymentsrestapi.i18n.Messages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.logging.log4j.util.Strings.isBlank;


// This way business rules are not tied to a framework.
public class HasValidDomainValues implements Validation {
    @Override
    public void validate(Payment payment, UUID idempotentKey) {
        assertIsGreaterThanZero("amount paid", payment.getAmountPaid().getAmount());
        assertIsNotBlankString("amount currency", payment.getAmountPaid().getCurrency());
        assertIsNotBlankString("status", payment.getStatus().getStatus());
        assertIsValidStatus(payment.getStatus().getStatus());
        assertIsGreaterThanZero("beneficiary id", payment.getBeneficiary().getId());
        assertIsNotBlankString("beneficiary name", payment.getBeneficiary().getName());
        assertIsGreaterThanZero("originator id", payment.getOriginator().getId());
        assertIsNotBlankString("originator name", payment.getOriginator().getName());
        assertIsNotBlankString("receiver account type", payment.getReceiver().getAccountType());
        assertIsGreaterThanZero("receiver account number", payment.getReceiver().getAccountNumber());
        assertIsNotBlankString("sender account type", payment.getSender().getAccountType());
        assertIsGreaterThanZero("sender account number", payment.getSender().getAccountNumber());
    }

    private void assertIsGreaterThanZero(String fieldName, Long id) {
        if (id <= 0) {
            throw new CustomInvalidField(Messages.Exceptions.INVALID_FIELD_LESS_OR_EQUAL_TO_ZERO(fieldName));
        }
    }

    private void assertIsValidStatus(String status) {
        List<String> validPaymentStatuses = new ArrayList<>();
        validPaymentStatuses.add(PAYMENT_STATUS.CREATED.getStatus());
        validPaymentStatuses.add(PAYMENT_STATUS.SENT.getStatus());
        validPaymentStatuses.add(PAYMENT_STATUS.REJECTED.getStatus());
        validPaymentStatuses.add(PAYMENT_STATUS.ACCEPTED.getStatus());


        boolean isValidStatus = validPaymentStatuses.stream().anyMatch(validStatus -> validStatus.equals(status));

        if (!isValidStatus) {
            throw new CustomInvalidField(Messages.Exceptions.INVALID_STATUS(status));
        }
    }

    private void assertIsNotBlankString(String fieldName, String string) {
        if (isBlank(string)) {
            throw new CustomInvalidField(Messages.Exceptions.INVALID_CANNOT_BE_BLANK(fieldName));
        }
    }

    private void assertIsGreaterThanZero(String fieldName, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new CustomInvalidField(Messages.Exceptions.INVALID_FIELD_LESS_OR_EQUAL_TO_ZERO(fieldName));
        }
    }
}
