package com.dev.exception;

public class InvalidPaymentSelectionException extends RuntimeException {
    public InvalidPaymentSelectionException(String message) {
        super(message);
    }
}