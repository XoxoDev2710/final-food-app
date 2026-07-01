package com.dev.model;


import com.dev.repository.PaymentStrategy;
import com.dev.util.ValidationUtil;

import java.util.Scanner;

public class UpiPaymentStrategy implements PaymentStrategy {

    private final Scanner scanner;

    public UpiPaymentStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("\n--- UPI PAYMENT ---");
        System.out.print("Enter your UPI ID (e.g., dev@oksbi): ");
        String upiId = scanner.nextLine().trim();

        if (!ValidationUtil.isValidUpiId(upiId)) {
            System.out.println("Invalid UPI ID format. Payment Failed.");
            return false;
        }

        System.out.print("Enter 4-digit UPI PIN to pay \u20B9" + String.format("%.2f", amount) + ": ");
        String pin = scanner.nextLine().trim();

        if (!ValidationUtil.isValidUpiPin(pin)) {
            System.out.println("Invalid PIN. Payment Failed.");
            return false;
        }

        System.out.println("Processing via Secure UPI Gateway...");
        System.out.println("Payment of \u20B9" + String.format("%.2f", amount) + " received!");
        return true;
    }
}

