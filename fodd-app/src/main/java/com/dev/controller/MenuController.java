package com.dev.controller;

import com.dev.enumm.FoodCategory;
import com.dev.exception.ItemNotFoundException;
import com.dev.model.FoodItem;
import com.dev.service.MenuService;
import com.dev.util.ValidationUtil;
import com.dev.view.MenuView;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuController {

    private final MenuService menuService;
    private final Scanner scanner;

    public MenuController(MenuService menuService, Scanner scanner) {
        this.menuService = menuService;
        this.scanner = scanner;
    }

    public void manageMenu()
    {
        while (true)
        {
            MenuView.adminMenuView();

            try
            {
                System.out.print("Choose operation: ");
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice)
                {
                    case 1: addFoodFlow(); break;
                    case 2: updateFoodFlow(); break;
                    case 3: toggleAvailabilityFlow(); break;
                    case 4: viewAllFlow(); break;
                    case 5: findByIdFlow(); break;
                    case 6: return;
                    default: System.out.println("Invalid choice.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
            catch (ItemNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void addFoodFlow()
    {
        System.out.print("Enter Food Name: ");
        String name = scanner.nextLine().trim();

        if (!ValidationUtil.isNonEmpty(name))
        {
            System.out.println("Food name cannot be empty.");
            return;
        }

        System.out.print("Enter Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        if (price <= 0)
        {
            System.out.println("Price must be greater than 0.");
            return;
        }

        FoodCategory category = selectCategory();

        FoodItem item = menuService.addFoodItem(name, price, category);
        System.out.println("Item added to menu.");
    }

    private void updateFoodFlow()
    {
        if (isMenuEmpty()) return;

        System.out.print("Enter Food ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        if (menuService.getItemById(id).isEmpty())
        {
            throw new ItemNotFoundException("Food ID not found.");
        }

        System.out.println("--- Enter New Details ---");
        System.out.print("Enter New Food Name: ");
        String newName = scanner.nextLine().trim();
        if (!ValidationUtil.isNonEmpty(newName))
        {
            System.out.println("Food name cannot be empty.");
            return;
        }

        System.out.print("Enter New Price: ");
        double newPrice = Double.parseDouble(scanner.nextLine().trim());
        if (newPrice <= 0)
        {
            System.out.println("Price must be greater than 0.");
            return;
        }

        FoodCategory newCategory = selectCategory();

        if (menuService.updateFoodItem(id, newName, newPrice, newCategory)) {
            System.out.println("Food Item updated!");
        }
    }

    private void toggleAvailabilityFlow() {
        if (isMenuEmpty()) return;

        System.out.print("Enter Food ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Optional<FoodItem> itemOpt = menuService.getItemById(id);
        if (itemOpt.isEmpty()) {
            throw new ItemNotFoundException("Food ID not found.");
        }

        FoodItem item = itemOpt.get();

        boolean targetAvailability = !item.isAvailable();

        menuService.setAvailability(id, targetAvailability);

        System.out.println("Item is now " + (targetAvailability ? "AVAILABLE" : "UNAVAILABLE") + " to customers.");
    }

    private void viewAllFlow()
    {
        List<FoodItem> items = menuService.getAllItems();

        if (items.isEmpty())
        {
            System.out.println("\nMenu is currently empty.");
        } else
        {
            System.out.println("\n--- CURRENT MENU ---");
            items.forEach(System.out::println);
        }
    }

    private void findByIdFlow() {
        if (isMenuEmpty()) return;

        System.out.print("Enter Food ID to search: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        Optional<FoodItem> itemOpt = menuService.getItemById(id);

        if (itemOpt.isPresent())
        {
            System.out.println("\nFound : " + itemOpt.get());
        } else
        {
            throw new ItemNotFoundException("Food ID not found.");
        }
    }

    private boolean isMenuEmpty()
    {
        if (menuService.getAllItems().isEmpty()) {
            System.out.println("\nThe menu is currently empty. Please add items first.");
            return true;
        }
        return false;
    }

    private FoodCategory selectCategory()
    {
        FoodCategory[] categories = FoodCategory.values();

        while (true)
        {
            System.out.println("\nSelect Category:");

            for (int i = 0; i < categories.length; i++)
            {
                System.out.println((i + 1) + ". " + categories[i]);
            }

            System.out.print("Enter choice (1-" + categories.length + "): ");

            try
            {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= categories.length)
                {
                    return categories[choice - 1];
                }
                System.out.println("Invalid choice. Select a valid category number.");
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}