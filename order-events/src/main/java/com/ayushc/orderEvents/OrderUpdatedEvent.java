package com.ayushc.orderEvents;

public record OrderUpdatedEvent (
        String id,
        String oldItemName,
        String newItemName,
        String oldStatus,
        String newStatus,
        String updatedAt
) {}
