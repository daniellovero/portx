package com.portx.dlovero.paymentsrestapi.services;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.transactional.UnitOfWork;
import com.portx.dlovero.paymentsrestapi.domain.validations.Validator;
import com.portx.dlovero.paymentsrestapi.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PaymentService {

    private final UnitOfWork unitOfWork;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(UnitOfWork unitOfWork, PaymentRepository paymentRepository) {
        this.unitOfWork = unitOfWork;
        this.paymentRepository = paymentRepository;
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.getById(id);
    }

    public boolean checkIfPaymentAlreadyExists(UUID idempotentKey) {
        return paymentRepository.paymentAlreadyExists(idempotentKey);
    }

    public Optional<Payment> saveAndPublishPayment(Payment payment, UUID idempotentKey) {
        AtomicReference<Optional<Payment>> savedPayment = new AtomicReference<>(Optional.empty());
        /*
         * This covers the case where if the save fails, or the "send" fails (even just before posting the message to the queue).
         * Which for the purpose of the exercise I think is good enough.
         * If it happens that between the instruction of committing to the db and after the "send fails, we'll need to look
         * For something more sophisticated such as distributed transactions. This is an assumption based that the `.send` is not atomic enough
         */
        unitOfWork.doInTransaction(() -> {
            new Validator(this).assertValidParameters(payment, idempotentKey);
            savedPayment.set(Optional.ofNullable(unitOfWork.save(payment, idempotentKey)));
            savedPayment.get().ifPresentOrElse(unitOfWork::send, () -> {
                throw new RuntimeException("Something went wrong while saving payment!");
            });
        });

        return savedPayment.get();
    }
}
