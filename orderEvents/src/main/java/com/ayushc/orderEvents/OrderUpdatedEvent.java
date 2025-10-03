package com.ayushc.orderEvents;

public record OrderUpdatedEvent(
        String orderId,
        String oldItemName,
        String newItemName,
        String oldStatus,
        String newStatus,
        String timestamp
) {}
