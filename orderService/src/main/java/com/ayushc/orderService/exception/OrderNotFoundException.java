package com.ayushc.orderService.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String id) {
        super("Order with ID " + id + " not found");
    }
}
