package com.ayushc.orderEvents;

public record OrderCreatedEvent(
        String orderId,
        String itemName,
        String status,
        String timestamp
) {}