package com.ayushc.orderService.exception;

import com.ayushc.orderService.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.lang.model.type.NullType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<BaseResponse<NullType>> handleOrderNotFound(OrderNotFoundException ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(null, ex.getMessage(), 404, "Order not found in DB"),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<NullType>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>(
                        null,
                        errorMessage,
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation failed"
                ));
    }

    //general fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<NullType>> handleGeneral(Exception ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(null, ex.getMessage(), 500, "Unexpected error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
