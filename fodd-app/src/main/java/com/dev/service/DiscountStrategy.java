package com.dev.service;

import com.dev.model.DiscountConfig;

public interface DiscountStrategy {
    double calculateDiscount(double cartTotal, DiscountConfig config);
}