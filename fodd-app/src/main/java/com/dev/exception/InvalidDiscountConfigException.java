package com.dev.exception;

public class InvalidDiscountConfigException extends RuntimeException {
    public InvalidDiscountConfigException(String message) {
        super(message);
    }
}