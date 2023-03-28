package com.portx.dlovero.paymentsrestapi.repositories;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.factories.PaymentFactory;
import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.MariaDBPaymentRepository;
import com.portx.dlovero.paymentsrestapi.repositories.implementation.mariadb.entities.PaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RelationalDatabasePaymentRepositoryTest {

    RelationalDatabasePaymentRepository paymentRepository;

    @Autowired
    private MariaDBPaymentRepository dbRepository;
    private PaymentFactory paymentFactory;

    @BeforeEach
    void setUp() {
        paymentRepository = new RelationalDatabasePaymentRepository(dbRepository);
        paymentFactory = new PaymentFactory();

    }

    @Test
    void testThatItSavesAPaymentInTheDatabase() {
        UUID validIdempotentKey = paymentFactory.getValidIdempotentKey();
        Payment paymentCreated = paymentRepository.save(paymentFactory.createValidPayment(), validIdempotentKey);

        assertThat(paymentCreated.getId()).isNotNull();
        assertThat(dbRepository.findByIdempotentKey(validIdempotentKey).isEmpty()).isFalse();
    }

    @Test
    void testThatItDoesNotSavesAPaymentInTheDatabaseIfItAlreadyExists() {
        UUID validIdempotentKey = paymentFactory.getValidIdempotentKey();
        Payment paymentCreated = paymentRepository.save(paymentFactory.createValidPayment(), validIdempotentKey);

        assertThat(paymentCreated.getId()).isNotNull();
        assertThat(dbRepository.findByIdempotentKey(validIdempotentKey).isEmpty()).isFalse();
    }

    @Test
    void testThatRetrievesAPaymentIfExistsInTheDatabase() {
        PaymentEntity savedPayment = dbRepository.save(new PaymentEntity(paymentFactory.createValidPayment(), paymentFactory.getValidIdempotentKey()));

        Optional<Payment> fetched = paymentRepository.getById(savedPayment.getId());

        assertThat(fetched).isPresent();
    }

    @Test
    void testThatRetrievesEmptyPaymentIfDoesNotExistsInTheDatabase() {

        Optional<Payment> fetched = paymentRepository.getById(-1L);

        assertThat(fetched).isNotPresent();
    }
}
