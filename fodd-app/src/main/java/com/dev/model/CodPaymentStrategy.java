package com.dev.model;


import com.dev.repository.PaymentStrategy;

public class CodPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("\n--- CASH ON DELIVERY ---");
        System.out.println("Please keep \u20B9" + String.format("%.2f", amount) + " ready at the time of delivery.");
        System.out.println("Order confirmed for COD.");
        return true;
    }
}

