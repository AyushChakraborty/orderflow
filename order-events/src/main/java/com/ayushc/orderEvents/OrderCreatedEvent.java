package com.ayushc.orderEvents;

public record OrderCreatedEvent (
        String id,
        String itemName,
        String status,
        String createdAt
) {}
