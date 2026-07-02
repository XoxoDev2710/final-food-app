package com.dev.controller;

import com.dev.exception.InvalidCredentialsException;
import com.dev.model.DeliveryGuy;
import com.dev.repository.*;
import com.dev.security.SecurityConfig;
import com.dev.service.CartService;
import com.dev.service.CustomerService;
import com.dev.service.DeliveryService;
import com.dev.service.MenuService;
import com.dev.service.OrderService;
import com.dev.service.DiscountService;
import com.dev.service.DiscountStrategy;
import com.dev.service.ThresholdPercentageDiscountStrategy;
import com.dev.factory.PaymentStrategyFactory;
import com.dev.factory.ConsolePaymentStrategyFactory;
import com.dev.view.FoodDeliverySystemView;

import java.util.Optional;
import java.util.Scanner;

public class ApplicationController
{

    public static void startApplication() {
        Scanner scanner = new Scanner(System.in);

        InMemoryFoodItemRepository foodItemRepository = new InMemoryFoodItemRepository();
        MenuService menuService = new MenuService(foodItemRepository);
        MenuController menuController = new MenuController(menuService, scanner);

        DeliveryApplicationRepository appRepo = new InMemoryDeliveryApplicationRepository();
        DeliveryGuyRepository deliveryGuyRepo = new InMemoryDeliveryGuyRepository();
        DeliveryService deliveryService = new DeliveryService(appRepo, deliveryGuyRepo);
        DeliveryApplicationController deliveryApplicationController = new DeliveryApplicationController(deliveryService, scanner);

        OrderRepository orderRepository = new InMemoryOrderRepository();
        OrderService orderService = new OrderService(orderRepository, deliveryGuyRepo);
        OrderController orderController = new OrderController(orderService, scanner);

        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        CustomerService customerService = new CustomerService(customerRepository);

        DiscountStrategy discountStrategy = new ThresholdPercentageDiscountStrategy();
        DiscountService discountService = new DiscountService(discountStrategy);
        DiscountController discountController = new DiscountController(discountService, scanner);

        CartService cartService = new CartService(menuService, orderService, discountService);

        PaymentStrategyFactory paymentStrategyFactory = new ConsolePaymentStrategyFactory();
        CustomerDashboardController customerDashboardController =
                new CustomerDashboardController(menuService, cartService, orderService, paymentStrategyFactory, scanner);
        CustomerAuthController customerAuthController = new CustomerAuthController(customerService, customerDashboardController, scanner);

        AdminController adminController = new AdminController(menuController, orderController, deliveryService, discountController, scanner);

        while (true)
        {
            FoodDeliverySystemView.systemView();
            try {

                System.out.print("Select an option: ");
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice)
                {
                    case 1: handleAdminLogin(adminController, scanner); break;
                    case 2: customerAuthController.startAuthMenu(); break;
                    case 3: handleDeliveryPortal(deliveryService, deliveryApplicationController, orderService, scanner); break;
                    case 4:
                        System.out.println("Shutting down the system. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select 1-4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

    }

    private static void handleAdminLogin(AdminController adminController, Scanner scanner)
    {
        try
        {
            System.out.print("Enter Admin Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter Admin Password: ");
            String password = scanner.nextLine().trim();

            if (SecurityConfig.ADMIN_USER.equals(username) && SecurityConfig.ADMIN_PASS.equals(password))
            {
                System.out.println("Login Successful!");
                adminController.showAdminMenu();
            }
            else
            {
                throw new InvalidCredentialsException("Invalid Credentials...");
            }
        }
        catch (InvalidCredentialsException exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    private static void handleDeliveryPortal(DeliveryService deliveryService,
                                             DeliveryApplicationController deliveryApplicationController,
                                             OrderService orderService,
                                             Scanner scanner) {
        boolean inDeliveryMenu = true;
        while (inDeliveryMenu) {
            System.out.println("\n--- DELIVERY AGENT PORTAL ---");
            System.out.println("1. Login");
            System.out.println("2. Apply for Delivery Job");
            System.out.println("3. Retrieve Account Details");
            System.out.println("4. Back");
            System.out.print("Select an option: ");

            try {
                int delChoice = Integer.parseInt(scanner.nextLine().trim());
                switch (delChoice) {
                    case 1: {
                        System.out.print("Enter Delivery Username: ");
                        String u = scanner.nextLine().trim();
                        System.out.print("Enter Delivery Password: ");
                        String p = scanner.nextLine().trim();

                        Optional<DeliveryGuy> guyOpt = deliveryService.authenticate(u, p);
                        if (guyOpt.isPresent()) {
                            System.out.println("Login Successful!");
                            DeliveryController deliveryController =
                                    new DeliveryController(orderService, guyOpt.get(), scanner);
                            deliveryController.showDashboard();
                        } else
                        {
                            throw new InvalidCredentialsException("Invalid Credentials...");
                        }
                        break;
                    }
                    case 2:
                        deliveryApplicationController.apply();
                        break;
                    case 3:
                        deliveryApplicationController.retrieveCredentials();
                        break;
                    case 4:
                        inDeliveryMenu = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1-4.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
            catch (InvalidCredentialsException exception)
            {
                System.out.println(exception.getMessage());
            }
        }
    }
}