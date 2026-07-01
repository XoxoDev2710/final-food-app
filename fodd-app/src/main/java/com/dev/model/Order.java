package com.dev.model;

import com.dev.enumm.OrderStatus;

import java.io.Serializable;
import java.util.Map;

public class Order
{
    private final int orderId;
    private final String customerUsername;
    private final Map<Integer, Integer> orderedItems;
    private final double totalAmount;
    private final double discountAmount;
    private OrderStatus status;
    private final String paymentMethod;
    private String assignedDeliveryGuyUsername;

    public Order(int orderId, String customerUsername, Map<Integer, Integer> orderedItems, double totalAmount, double discountAmount, OrderStatus status, String paymentMethod)
    {
        this.orderId = orderId;
        this.customerUsername = customerUsername;
        this.orderedItems = orderedItems;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public int getOrderId() { return orderId; }

    public String getCustomerUsername() { return customerUsername; }

    public Map<Integer, Integer> getOrderedItems() { return orderedItems; }

    public double getTotalAmount() { return totalAmount; }

    public double getDiscountAmount() { return discountAmount; }

    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }

    public String getAssignedDeliveryGuyUsername() { return assignedDeliveryGuyUsername; }

    public void setAssignedDeliveryGuyUsername(String assignedDeliveryGuyUsername)
    {
        this.assignedDeliveryGuyUsername = assignedDeliveryGuyUsername;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " | Customer: " + customerUsername +
                (discountAmount > 0 ? " | Discount: \u20B9" + String.format("%.2f", discountAmount) : "") +
                " | Total: \u20B9" + String.format("%.2f", totalAmount) +
                " | Payment: " + paymentMethod +
                " | Status: " + status +
                (assignedDeliveryGuyUsername != null ? " | Agent: " + assignedDeliveryGuyUsername : "");
    }
}