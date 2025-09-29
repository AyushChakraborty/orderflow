package com.ayushc.orderEvents;

public record OrderDeletedEvent (
        String id,
        String deletedAt
) {}
