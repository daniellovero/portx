package com.portx.dlovero.paymentsrestapi.repositories;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.MariaDBPaymentRepository;
import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.entities.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


// If you use a relational database for persistence we should not tie it up to a particular implementation of it.
@Component
public class RelationalDatabasePaymentRepository implements PaymentRepository {

    // So this can be switched to other kinds of relational DBs if they all implement the JpaRepository contract and we won't notice the change.
    private final MariaDBPaymentRepository repository;

    @Autowired
    public RelationalDatabasePaymentRepository(MariaDBPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Payment> getById(Long id) {
        return repository.findById(id).map(PaymentEntity::toModel);
    }

    @Override
    public Payment save(Payment payment, UUID idempotentKey) {
        PaymentEntity paymentEntity = new PaymentEntity(payment, idempotentKey);
        return repository.save(paymentEntity).toModel();
    }

    @Override
    public boolean paymentAlreadyExists(UUID idempotentKey) {
        return !repository.findByIdempotentKey(idempotentKey).isEmpty();
    }
}
