package com.dev.controller;

import com.dev.exception.InvalidDiscountConfigException;
import com.dev.service.DiscountService;
import com.dev.view.DiscountView;

import java.util.Scanner;

public class DiscountController {

    private final DiscountService discountService;
    private final Scanner scanner;

    public DiscountController(DiscountService discountService, Scanner scanner) {
        this.discountService = discountService;
        this.scanner = scanner;
    }

    public void manageDiscount() {
        while (true) {
            DiscountView.discountMenuView(discountService.getCurrentConfig());
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: updateDiscountFlow(); break;
                    case 2: return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void updateDiscountFlow() {
        try {
            System.out.print("Enter Minimum Cart Amount to trigger discount (e.g. 500): ");
            double thresholdAmount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Discount Percentage (0-100): ");
            double percentage = Double.parseDouble(scanner.nextLine().trim());

            discountService.updateConfig(thresholdAmount, percentage);
            System.out.println("Discount configuration updated! It will apply to all carts going forward.");
        }
        catch (NumberFormatException e)
        {
            System.out.println("Please enter valid numeric values.");
        }
        catch (InvalidDiscountConfigException e)
        {
            System.out.println(e.getMessage());
        }
    }
}