package com.portx.dlovero.paymentsrestapi.repositories;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


// What you use for persistence is up to you, we just need this protocol.
@Repository
public interface PaymentRepository {

    Optional<Payment> getById(Long id);

    Payment save(Payment payment, UUID idempotentKey);

    boolean paymentAlreadyExists(UUID idempotentKey);
}
