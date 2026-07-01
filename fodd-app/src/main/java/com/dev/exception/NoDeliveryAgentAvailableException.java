package com.dev.exception;

public class NoDeliveryAgentAvailableException extends RuntimeException {
    public NoDeliveryAgentAvailableException(String message) {
        super(message);
    }
}