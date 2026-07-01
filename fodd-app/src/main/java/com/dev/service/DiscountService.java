package com.dev.service;

import com.dev.exception.InvalidDiscountConfigException;
import com.dev.model.DiscountConfig;

public class DiscountService {

    private final DiscountConfig currentConfig;
    private final DiscountStrategy discountStrategy;

    public DiscountService(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
        this.currentConfig = new DiscountConfig(0.0, 0.0); // no discount by default
    }

    public DiscountConfig getCurrentConfig() {
        return currentConfig;
    }

    public void updateConfig(double thresholdAmount, double discountPercentage) {
        if (thresholdAmount < 0) {
            throw new InvalidDiscountConfigException("Threshold amount cannot be negative.");
        }
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new InvalidDiscountConfigException("Discount percentage must be between 0 and 100.");
        }
        currentConfig.update(thresholdAmount, discountPercentage);
    }

    public double calculateDiscount(double cartTotal) {
        return discountStrategy.calculateDiscount(cartTotal, currentConfig);
    }
}