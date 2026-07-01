package com.dev.view;

public class OrderStatusView
{
    public static void orderStatusView()
    {
        System.out.println("Select New Status:");
        System.out.println("1. ACCEPTED");
        System.out.println("2. REJECTED");
        System.out.println("3. PREPARING");
        System.out.println("4. READY_FOR_DELIVERY (auto-assigns a free agent)");
    }
}

