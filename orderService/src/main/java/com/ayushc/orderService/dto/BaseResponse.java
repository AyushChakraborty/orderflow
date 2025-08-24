package com.ayushc.orderService.dto;

public record BaseResponse<T> (
    T data,
    String message,
    int status,
    String details
) {}
