package com.dev.view;

import com.dev.model.DiscountConfig;

public class DiscountView {

    public static void discountMenuView(DiscountConfig config) {
        System.out.println("\n=== DISCOUNT CONFIGURATION ===");
        System.out.println("Current Setting: " + config);
        System.out.println("1. Update Discount (Threshold Amount & Percentage)");
        System.out.println("2. Back to Admin Dashboard");
    }
}