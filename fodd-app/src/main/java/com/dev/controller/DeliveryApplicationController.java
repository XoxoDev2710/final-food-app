package com.dev.controller;

import com.dev.exception.ApplicationNotFoundException;
import com.dev.exception.DuplicateApplicationException;
import com.dev.model.DeliveryApplication;
import com.dev.model.DeliveryGuy;
import com.dev.service.DeliveryService;
import com.dev.util.ValidationUtil;

import java.util.Scanner;

public class DeliveryApplicationController {

    private final DeliveryService deliveryService;
    private final Scanner scanner;

    public DeliveryApplicationController(DeliveryService deliveryService, Scanner scanner)
    {
        this.deliveryService = deliveryService;
        this.scanner = scanner;
    }

    public void apply()
    {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        if (!ValidationUtil.isNonEmpty(name))
        {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();

        if (!ValidationUtil.isValidPhone(phone))
        {
            System.out.println("Invalid phone number format.");
            return;
        }

        try
        {
            DeliveryApplication application = deliveryService.submitApplication(name, phone);

            System.out.println("\nApplication submitted!");
            System.out.println("-----------------------------------------------------------------");
            System.out.println("YOUR UNIQUE APPLICATION ID: " + application.getApplicationId());
            System.out.println("Please SAVE this ID. You will need it to retrieve");
            System.out.println("your username and password after Admin approval.");
            System.out.println("------------------------------------------------------------------");
        }
        catch (DuplicateApplicationException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void retrieveCredentials()
    {
        System.out.print("Enter your Application ID: ");

        try
        {
            int appId = Integer.parseInt(scanner.nextLine().trim());

            DeliveryGuy guy = deliveryService.retrieveCredentials(appId);

            if (guy != null) {
                System.out.println("\nHere are your credentials:");
                System.out.println(">>> Username: " + guy.getUsername());
                System.out.println(">>> Password: " + guy.getPassword());
            } else {
                System.out.println("Your application is still pending Admin approval.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Application ID must be a number.");
        }
        catch (ApplicationNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
}