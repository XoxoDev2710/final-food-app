package com.dev.controller;

import com.dev.exception.EmptyCartException;
import com.dev.exception.InvalidPaymentSelectionException;
import com.dev.exception.ItemNotFoundException;
import com.dev.factory.PaymentOption;
import com.dev.factory.PaymentStrategyFactory;
import com.dev.model.Cart;
import com.dev.model.Customer;
import com.dev.model.FoodItem;
import com.dev.model.Order;
import com.dev.service.CartService;
import com.dev.service.MenuService;
import com.dev.service.OrderService;
import com.dev.view.CustomerDashboardView;
import com.dev.view.InvoiceView;

import java.util.List;
import java.util.Scanner;

public class CustomerDashboardController
{
    private final MenuService menuService;
    private final CartService cartService;
    private final OrderService orderService;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final Scanner scanner;

    public CustomerDashboardController(MenuService menuService, CartService cartService,
                                       OrderService orderService, PaymentStrategyFactory paymentStrategyFactory,
                                       Scanner scanner) {
        this.menuService = menuService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.paymentStrategyFactory = paymentStrategyFactory;
        this.scanner = scanner;
    }

    public void showDashboard(Customer loggedInCustomer)
    {
        String username = loggedInCustomer.getUsername();
        cartService.pruneUnavailableItems(username);

        while (true)
        {
            CustomerDashboardView.customerDashboardView(loggedInCustomer);
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice)
                {
                    case 1: browseMenuAndAddToCart(loggedInCustomer); break;
                    case 2: viewCartAndCheckout(loggedInCustomer); break;
                    case 3: viewOrderHistory(loggedInCustomer); break;
                    case 4:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void browseMenuAndAddToCart(Customer customer)
    {
        List<FoodItem> availableItems = menuService.getAvailableItems();

        if (availableItems.isEmpty())
        {
            System.out.println("\nSorry, no food items are currently available.");
            return;
        }

        while (true) {
            System.out.println("\n--- TODAY'S MENU ---");
            availableItems.forEach(System.out::println);

            System.out.print("\nEnter Food ID to add to cart (or 0 to go back): ");

            try
            {
                int foodId = Integer.parseInt(scanner.nextLine().trim());
                if (foodId == 0) return;

                System.out.print("Enter Quantity (max " + Cart.MAX_QTY_PER_ITEM + " per item): ");
                int qty = Integer.parseInt(scanner.nextLine().trim());

                if (qty <= 0)
                {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }

                boolean added = cartService.addItemToCart(customer.getUsername(), foodId, qty);
                if (added)
                {
                    String itemName = menuService.getItemById(foodId).map(FoodItem::getFoodName).orElse("Item");
                    System.out.println(qty + " x " + itemName + " added to cart!");
                }
                else
                {
                    System.out.println("Cannot add. Max allowed quantity per item is " + Cart.MAX_QTY_PER_ITEM + ".");
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

    private void viewCartAndCheckout(Customer customer)
    {
        String username = customer.getUsername();

        while (true)
        {
            Cart cart = cartService.getCart(username);

            if (cart.isEmpty())
            {
                System.out.println("\nYour cart is empty.");
                return;
            }

            cartService.pruneUnavailableItems(username);

            double subtotal = cartService.calculateCartTotal(username);
            double discount = cartService.calculateDiscount(username);
            double grandTotal = subtotal - discount;

            InvoiceView.printCartInvoice(cart, menuService, subtotal, discount, grandTotal);

            System.out.println("\n1. Proceed to Checkout & Pay");
            System.out.println("2. Remove an Item");
            System.out.println("3. Clear Entire Cart");
            System.out.println("4. Go Back");
            System.out.print("Choose option: ");

            try
            {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice)
                {
                    case 1:
                        executeCheckoutFlow(customer);
                        if (cartService.getCart(username).isEmpty()) return;
                        break;
                    case 2:
                        System.out.print("Enter Food ID to remove: ");
                        int removeId = Integer.parseInt(scanner.nextLine().trim());
                        if (cart.getItems().containsKey(removeId)) {
                            cartService.removeItemFromCart(username, removeId);
                            System.out.println("Item removed from cart.");
                        }
                        else
                        {
                            System.out.println("ID not found in cart.");
                        }
                        break;
                    case 3:
                        cartService.clearCart(username);
                        System.out.println("Cart cleared.");
                        return;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void executeCheckoutFlow(Customer customer)
    {
        System.out.println("\n--- SELECT PAYMENT METHOD ---");
        System.out.println("1. UPI");
        System.out.println("2. Cash on Delivery (COD)");
        System.out.print("Choice: ");

        try
        {
            int payChoice = Integer.parseInt(scanner.nextLine().trim());

            PaymentOption paymentOption = paymentStrategyFactory.createPaymentOption(payChoice, scanner);

            Order newOrder = cartService.checkout(customer.getUsername(), paymentOption.getStrategy(),
                    paymentOption.getMethodName());

            if (newOrder != null)
            {
                System.out.println("Order Placed! Your Order ID is #" + newOrder.getOrderId());
                if (newOrder.getDiscountAmount() > 0) {
                    System.out.printf("You saved \u20B9%.2f with the current discount offer!%n", newOrder.getDiscountAmount());
                }
            }
            else
            {
                System.out.println("Order was not placed due to payment failure.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Please enter a valid number.");
        }
        catch (InvalidPaymentSelectionException e)
        {
            System.out.println(e.getMessage());
        }
        catch (EmptyCartException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void viewOrderHistory(Customer customer)
    {
        List<Order> history = orderService.getOrdersByCustomer(customer.getUsername());
        if (history.isEmpty())
        {
            System.out.println("\nYou have no past orders.");
        }
        else
        {
            System.out.println("\n--- ORDER HISTORY ---");
            history.forEach(System.out::println);
        }
    }
}