package com.dev.service;

import com.dev.model.DiscountConfig;

public class ThresholdPercentageDiscountStrategy implements DiscountStrategy
{

    @Override
    public double calculateDiscount(double cartTotal, DiscountConfig config) {
        if (!config.isConfigured()) {
            return 0.0;
        }
        if (cartTotal < config.getThresholdAmount()) {
            return 0.0;
        }
        return cartTotal * (config.getDiscountPercentage() / 100.0);
    }
}