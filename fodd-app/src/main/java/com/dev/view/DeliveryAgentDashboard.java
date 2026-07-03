package com.dev.view;

import com.dev.model.DeliveryGuy;

public class DeliveryAgentDashboard
{
    public static void agentDashboard(DeliveryGuy deliveryGuy)
    {
        System.out.println("\n=== DELIVERY AGENT DASHBOARD: " + deliveryGuy.getName().toUpperCase() + " ===");
        System.out.println("1. View Ready Orders");
        System.out.println("2. Pick Up Order (Change to OUT_FOR_DELIVERY)");
        System.out.println("3. Mark Delivered (Change to DELIVERED)");
        System.out.println("4. Logout");
        System.out.print("Select an option: ");
    }
}
