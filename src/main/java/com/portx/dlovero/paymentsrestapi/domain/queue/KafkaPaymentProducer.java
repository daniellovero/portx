package com.portx.dlovero.paymentsrestapi.domain.queue;

import com.portx.dlovero.paymentsrestapi.config.KafkaConfig;
import com.portx.dlovero.paymentsrestapi.domain.model.Payment;
import com.portx.dlovero.paymentsrestapi.domain.queue.serializers.PaymentSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaPaymentProducer implements PaymentProducer {
    private final String paymentTopic;
    private final Producer<String, Payment> producer;

    @Autowired
    public KafkaPaymentProducer(KafkaConfig kafkaConfig) {
        this.paymentTopic = kafkaConfig.getTopic();
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PaymentSerializer.class.getName());
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void sendPayment(Payment payment) {
        producer.send(new ProducerRecord<>(paymentTopic, payment));
    }
}
