package com.ayushc.orderEvents;

public record OrderDeletedEvent(
        String orderId,
        String itemName,
        String status,
        String timestamp
) {}