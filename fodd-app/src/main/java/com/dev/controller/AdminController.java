package com.dev.controller;

import com.dev.exception.ApplicationNotFoundException;
import com.dev.exception.UsernameTakenException;
import com.dev.model.DeliveryApplication;
import com.dev.model.DeliveryGuy;
import com.dev.service.DeliveryService;
import com.dev.util.ValidationUtil;
import com.dev.view.AdminDashboardView;

import java.util.List;
import java.util.Scanner;

public class AdminController {

    private final MenuController menuController;
    private final OrderController orderController;
    private final DeliveryService deliveryService;
    private final DiscountController discountController;
    private final Scanner scanner;

    public AdminController(MenuController menuController, OrderController orderController,
                           DeliveryService deliveryService, DiscountController discountController,
                           Scanner scanner) {
        this.menuController = menuController;
        this.orderController = orderController;
        this.deliveryService = deliveryService;
        this.discountController = discountController;
        this.scanner = scanner;
    }

    public void showAdminMenu()
    {
        while (true)
        {
            AdminDashboardView.adminView();

            try
            {
                System.out.print("Select an option (Enter number): ");
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice)
                {
                    case 1: menuController.manageMenu(); break;
                    case 2: orderController.manageOrders(); break;
                    case 3: handleDeliveryApplications(); break;
                    case 4: discountController.manageDiscount(); break;
                    case 5:
                        System.out.println("Logging out...");
                        return;
                    default: System.out.println("Invalid choice.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void handleDeliveryApplications()
    {
        List<DeliveryApplication> apps = deliveryService.getPendingApplications();

        if (apps.isEmpty())
        {
            System.out.println("No pending delivery agent applications.");
            return;
        }

        System.out.println("\n--- PENDING APPLICATIONS ---");
        apps.forEach(System.out::println);

        try
        {
            System.out.print("Enter Application ID to approve (or 0 to back): ");
            int appId = Integer.parseInt(scanner.nextLine().trim());
            if (appId == 0) return;

            deliveryService.getApplicationOrThrow(appId);

            String username;
            while (true) {
                System.out.print("Set Permanent Username (min 4 chars, non-numeric): ");
                username = scanner.nextLine().trim();
                if (ValidationUtil.isValidUsername(username) && !deliveryService.isUsernameTaken(username))
                {
                    break;
                }
                System.out.println("Invalid or already-taken username. Try again.");
            }

            String password;
            while (true) {
                System.out.print("Set Permanent Password (min 6 chars): ");
                password = scanner.nextLine().trim();
                if (ValidationUtil.isValidPassword(password))
                {
                    break;
                }
                System.out.println("Password must be at least 6 characters.");
            }

            DeliveryGuy newAgent = deliveryService.approveApplication(appId, username, password);

            System.out.println("Agent approved (" + newAgent.getUsername() + ")! They can now log in, " +
                    "or retrieve these credentials using Application ID #" + appId);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Please enter a valid number.");
        }
        catch (ApplicationNotFoundException | UsernameTakenException e)
        {
            System.out.println(e.getMessage());
        }
    }
}