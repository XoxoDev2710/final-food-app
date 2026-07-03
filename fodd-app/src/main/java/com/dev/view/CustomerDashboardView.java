package com.dev.view;

import com.dev.model.Customer;

public class CustomerDashboardView
{
    public static void customerDashboardView(Customer loggedInCustomer)
    {
        System.out.println("\n=== CUSTOMER DASHBOARD: " + loggedInCustomer.getName().toUpperCase() + " ===");
        System.out.println("1. Browse Menu");
        System.out.println("2. View Cart & Checkout");
        System.out.println("3. View Order History");
        System.out.println("4. Logout");
    }
}
