package com.portx.dlovero.paymentsrestapi.domain.queue;

import com.portx.dlovero.paymentsrestapi.domain.model.Payment;

public interface PaymentProducer {

    void sendPayment(Payment payment);
}
