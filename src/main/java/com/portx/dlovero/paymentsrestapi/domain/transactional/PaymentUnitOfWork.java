package com.portx.dlovero.paymentsrestapi.domain.transactional;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.queue.PaymentProducer;
import com.portx.dlovero.paymentsrestapi.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentUnitOfWork implements UnitOfWork {

    private final PaymentProducer paymentProducer;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentUnitOfWork(PaymentProducer paymentProducer, PaymentRepository paymentRepository) {
        this.paymentProducer = paymentProducer;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment save(Payment payment, UUID idempotentKey) {
        return paymentRepository.save(payment, idempotentKey);
    }

    @Override
    public void send(Payment payment) {
        paymentProducer.sendPayment(payment);
    }

    @Override
    @Transactional
    public void doInTransaction(Runnable action) {
        action.run();
    }
}
