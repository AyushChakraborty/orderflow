package com.ayushc.notificationService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "orders", groupId = "notification-service")
    public void consume(String message) {
        System.out.println("Received raw event: " + message);

        try {
            //first, parse into generic JSON
            var jsonNode = mapper.readTree(message);
            var eventType = jsonNode.get("status").asText();

            //next, handle based on event type
            switch (eventType.toLowerCase()) {
                case "created" -> System.out.println("New order created: " + jsonNode);
                case "updated" -> System.out.println("Order updated: " + jsonNode);
                case "deleted" -> System.out.println("Order deleted: " + jsonNode);
                default -> System.out.println("Unknown event type: " + jsonNode);
            }
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }
}
