package com.dev.factory;

import com.dev.repository.PaymentStrategy;

public class PaymentOption {
    private final PaymentStrategy strategy;
    private final String methodName;

    public PaymentOption(PaymentStrategy strategy, String methodName) {
        this.strategy = strategy;
        this.methodName = methodName;
    }

    public PaymentStrategy getStrategy() {
        return strategy;
    }

    public String getMethodName() {
        return methodName;
    }
}