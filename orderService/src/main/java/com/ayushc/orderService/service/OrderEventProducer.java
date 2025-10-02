package com.ayushc.orderService.service;

import com.ayushc.orderEvents.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(OrderEvent event) {
        //orderId is the key, ensures per-order ordering
        kafkaTemplate.send("orders", event.orderId(), event);
    }
}
