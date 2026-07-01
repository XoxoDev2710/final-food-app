package com.dev.model;

import java.io.Serializable;

public class DiscountConfig
{

    private double thresholdAmount;
    private double discountPercentage;

    public DiscountConfig(double thresholdAmount, double discountPercentage) {
        this.thresholdAmount = thresholdAmount;
        this.discountPercentage = discountPercentage;
    }

    public double getThresholdAmount() {
        return thresholdAmount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void update(double thresholdAmount, double discountPercentage) {
        this.thresholdAmount = thresholdAmount;
        this.discountPercentage = discountPercentage;
    }

    public boolean isConfigured() {
        return thresholdAmount > 0 && discountPercentage > 0;
    }

    @Override
    public String toString() {
        if (!isConfigured()) {
            return "No discount configured (discount is \u20B90.00 for all orders).";
        }
        return String.format("Spend \u20B9%.2f or more to get %.2f%% OFF", thresholdAmount, discountPercentage);
    }
}