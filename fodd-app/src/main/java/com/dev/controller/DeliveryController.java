package com.dev.controller;

import com.dev.enumm.OrderStatus;
import com.dev.exception.OrderNotFoundException;
import com.dev.model.DeliveryGuy;
import com.dev.model.Order;
import com.dev.service.OrderService;

import java.util.List;
import java.util.Scanner;

public class DeliveryController {

    private final OrderService orderService;
    private final DeliveryGuy deliveryGuy;
    private final Scanner scanner;

    public DeliveryController(OrderService orderService, DeliveryGuy deliveryGuy, Scanner scanner) {
        this.orderService = orderService;
        this.deliveryGuy = deliveryGuy;
        this.scanner = scanner;
    }

    public void showDashboard()
    {
        while (true)
        {
            System.out.println("\n=== DELIVERY AGENT DASHBOARD: " + deliveryGuy.getName() + " ===");
            System.out.println("1. View Ready Orders");
            System.out.println("2. Pick Up Order (Change to OUT_FOR_DELIVERY)");
            System.out.println("3. Mark Delivered (Change to DELIVERED)");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");

            try
            {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice)
                {
                    case 1: viewMyOrders(); break;
                    case 2: handlePickUp(); break;
                    case 3: handleDelivery(); break;
                    case 4:
                        System.out.println("Logging out...");
                        return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void viewMyOrders()
    {
        List<Order> myOrders = orderService.getOrdersForAgent(deliveryGuy.getUsername(), OrderStatus.READY_FOR_DELIVERY);
        if (myOrders.isEmpty()) {
            System.out.println("No orders are currently assigned to you.");
        } else {
            System.out.println("\n--- YOUR READY ORDERS ---");
            myOrders.forEach(System.out::println);
        }
    }

    private void handlePickUp()
    {
        List<Order> myOrders = orderService.getOrdersForAgent(deliveryGuy.getUsername(), OrderStatus.READY_FOR_DELIVERY);
        if (myOrders.isEmpty())
        {
            System.out.println("No orders assigned to you to pick up.");
            return;
        }

        myOrders.forEach(System.out::println);
        System.out.print("Enter Order ID to Pick Up: ");

        try
        {
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            if (orderService.pickupOrder(orderId, deliveryGuy.getUsername()))
            {
                System.out.println("Order #" + orderId + " picked up! Status set to OUT_FOR_DELIVERY.");
            }
            else
            {
                System.out.println("Failed to update order status.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Order ID must be a number.");
        }
        catch (OrderNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void handleDelivery()
    {
        List<Order> myOrders = orderService.getOrdersForAgent(deliveryGuy.getUsername(), OrderStatus.OUT_FOR_DELIVERY);
        if (myOrders.isEmpty())
        {
            System.out.println("You have no orders out for delivery.");
            return;
        }

        myOrders.forEach(System.out::println);
        System.out.print("Enter Order ID to Mark Delivered: ");

        try
        {
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            if (orderService.deliverOrder(orderId, deliveryGuy))
            {
                System.out.println("Order #" + orderId + " marked as DELIVERED!");
            }
            else
            {
                System.out.println("Failed to update order status.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Order ID must be a number.");
        }
        catch (OrderNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
}