package com.dev.controller;


import com.dev.enumm.OrderStatus;
import com.dev.exception.NoDeliveryAgentAvailableException;
import com.dev.model.Order;
import com.dev.service.OrderService;
import com.dev.view.OrderView;

import java.util.List;
import java.util.Scanner;

public class OrderController {

    private final OrderService orderService;
    private final Scanner scanner;

    public OrderController(OrderService orderService, Scanner scanner) {
        this.orderService = orderService;
        this.scanner = scanner;
    }

    public void manageOrders()
    {
        while (true)
        {
            List<Order> activeOrders = orderService.getActiveAdminOrders();
            if (activeOrders.isEmpty())
            {
                System.out.println("No active orders require attention right now.");
                return;
            }

            System.out.println("\n--- ACTIVE ORDERS ---");
            activeOrders.forEach(System.out::println);

            OrderView.customerOrderView();
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: updateOrderStatusFlow(); break;
                    case 2: return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void updateOrderStatusFlow()
    {
        try {
            System.out.print("Enter Order ID to update: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());

            if (orderService.getOrderById(orderId).isEmpty())
            {
                System.out.println("Order ID not found.");
                return;
            }

            System.out.println("Select New Status:");
            System.out.println("1. ACCEPTED");
            System.out.println("2. REJECTED");
            System.out.println("3. PREPARING");
            System.out.println("4. READY_FOR_DELIVERY (auto-assigns a free agent)");
            System.out.print("Choice: ");

            int statusChoice = Integer.parseInt(scanner.nextLine().trim());
            OrderStatus newStatus;
            switch (statusChoice) {
                case 1: newStatus = OrderStatus.ACCEPTED; break;
                case 2: newStatus = OrderStatus.REJECTED; break;
                case 3: newStatus = OrderStatus.PREPARING; break;
                case 4: newStatus = OrderStatus.READY_FOR_DELIVERY; break;
                default:
                    System.out.println("Invalid status choice.");
                    return;
            }

            if (orderService.updateOrderStatus(orderId, newStatus))
            {
                System.out.println("Order #" + orderId + " is now " + newStatus);
            }
            else
            {
                System.out.println("Failed to update order.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Please enter valid numbers.");
        }
        catch (NoDeliveryAgentAvailableException e)
        {
            // Order status was intentionally left unchanged by the service.
            System.out.println(e.getMessage());
        }
    }
}