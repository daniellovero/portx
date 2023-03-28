package com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb;

import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MariaDBPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByIdempotentKey(UUID idempotentKey);
}
