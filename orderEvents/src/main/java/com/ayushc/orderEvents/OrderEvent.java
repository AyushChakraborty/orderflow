package com.ayushc.orderEvents;

public record OrderEvent(
        String eventType,
        String orderId,
        String itemName,
        String status,
        String timestamp
) {}
