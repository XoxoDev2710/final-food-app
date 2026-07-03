package com.dev.controller;

import com.dev.model.Customer;
import com.dev.service.CustomerService;
import com.dev.util.ValidationUtil;
import com.dev.view.CustomerAuthView;

import java.util.Optional;
import java.util.Scanner;

public class CustomerAuthController {

    private final CustomerService customerService;
    private final CustomerDashboardController dashboardController;
    private final Scanner scanner;

    public CustomerAuthController(CustomerService customerService, CustomerDashboardController dashboardController, Scanner scanner) {
        this.customerService = customerService;
        this.dashboardController = dashboardController;
        this.scanner = scanner;
    }

    public void startAuthMenu()
    {
        while (true)
        {
            CustomerAuthView.customerAuthView();

            try
            {
                System.out.print("Select an option: ");
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice)
                {
                    case 1: loginFlow(); break;
                    case 2: signUpFlow(); break;
                    case 3: return;
                    default: System.out.println("Invalid choice.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void loginFlow()
    {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        Optional<Customer> authCustomer = customerService.authenticate(username, password);

        if (authCustomer.isPresent())
        {
            System.out.println("Login Successful!");
            dashboardController.showDashboard(authCustomer.get());
        }
        else
        {
            System.out.println("Invalid Username or Password.");
        }
    }


    private void signUpFlow()
    {
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        if (!ValidationUtil.isNonEmpty(name))
        {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Choose a Username (min 4 chars, non-numeric): ");
        String username = scanner.nextLine().trim();
        if (!ValidationUtil.isValidUsername(username))
        {
            System.out.println("Invalid username. Must be at least 4 characters and not purely numeric.");
            return;
        }

        System.out.print("Choose a Password (min 6 chars): ");
        String password = scanner.nextLine().trim();
        if (!ValidationUtil.isValidPassword(password))
        {
            System.out.println("Password must be at least 6 characters.");
            return;
        }

        System.out.print("Enter Full Delivery Address (house no, street, city, pincode): ");
        String address = scanner.nextLine().trim();
        if (!ValidationUtil.isNonEmpty(address))
        {
            System.out.println("Address cannot be empty.");
            return;
        }

        if (customerService.registerCustomer(name, username, password, address))
        {
            System.out.println("Account created! You can now login.");
        }
        else
        {
            System.out.println("Username already taken. Please try another.");
        }
    }
}