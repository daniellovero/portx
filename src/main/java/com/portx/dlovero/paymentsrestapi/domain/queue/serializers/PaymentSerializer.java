package com.portx.dlovero.paymentsrestapi.domain.queue.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class PaymentSerializer implements Serializer<Payment> {

    @Override
    public byte[] serialize(String topic, Payment payment) {
        if (payment == null) {
            return new byte[0];
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(payment);
        } catch (Exception e) {
            throw new SerializationException("Error serializing payment", e);
        }
    }

}
