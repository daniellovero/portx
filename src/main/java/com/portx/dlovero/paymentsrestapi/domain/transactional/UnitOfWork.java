package com.portx.dlovero.paymentsrestapi.domain.transactional;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;

import java.util.UUID;

public interface UnitOfWork {
    Payment save(Payment payment, UUID idempotentKey);

    void send(Payment payment);

    void doInTransaction(Runnable action);
}
