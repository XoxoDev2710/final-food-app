package com.dev.service;

import com.dev.exception.EmptyCartException;
import com.dev.exception.ItemNotFoundException;
import com.dev.model.Cart;
import com.dev.model.FoodItem;
import com.dev.model.Order;
import com.dev.repository.PaymentStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CartService {

    private final Map<String, Cart> customerCarts = new HashMap<>();
    private final MenuService menuService;
    private final OrderService orderService;
    private final DiscountService discountService;

    public CartService(MenuService menuService, OrderService orderService, DiscountService discountService) {
        this.menuService = menuService;
        this.orderService = orderService;
        this.discountService = discountService;
    }

    public Cart getCart(String username) {
        return customerCarts.computeIfAbsent(username, u -> new Cart());
    }

    public boolean addItemToCart(String username, int foodId, int quantity) {
        Optional<FoodItem> itemOpt = menuService.getItemById(foodId);
        if (itemOpt.isEmpty() || !itemOpt.get().isAvailable()) {
            throw new ItemNotFoundException("Invalid Food ID or item is currently unavailable.");
        }
        return getCart(username).addItem(foodId, quantity);
    }

    public void removeItemFromCart(String username, int foodId) {
        getCart(username).removeItem(foodId);
    }

    public void clearCart(String username) {
        getCart(username).clearCart();
    }

    public double calculateCartTotal(String username) {
        Cart cart = getCart(username);
        double total = 0;
        for (Map.Entry<Integer, Integer> entry : cart.getItems().entrySet()) {
            Optional<FoodItem> itemOpt = menuService.getItemById(entry.getKey());
            if (itemOpt.isPresent()) {
                total += itemOpt.get().getPrice() * entry.getValue();
            }
        }
        return total;
    }

    public double calculateDiscount(String username) {
        return discountService.calculateDiscount(calculateCartTotal(username));
    }

    public double calculateGrandTotal(String username) {
        double subtotal = calculateCartTotal(username);
        return subtotal - discountService.calculateDiscount(subtotal);
    }

    public void pruneUnavailableItems(String username) {
        Cart cart = getCart(username);
        cart.getItems().keySet().removeIf(foodId -> menuService.getItemById(foodId).isEmpty());
    }

    public Order checkout(String username, PaymentStrategy paymentStrategy, String paymentMethodName) {
        Cart cart = getCart(username);
        if (cart.isEmpty()) {
            throw new EmptyCartException("Your cart is empty.");
        }

        double subtotal = calculateCartTotal(username);
        double discount = discountService.calculateDiscount(subtotal);
        double grandTotal = subtotal - discount;

        if (!paymentStrategy.processPayment(grandTotal)) {
            return null;
        }

        Map<Integer, Integer> orderItems = new HashMap<>(cart.getItems());
        Order order = orderService.placeOrder(username, orderItems, grandTotal, discount, paymentMethodName);

        if (order != null) {
            clearCart(username);
        }
        return order;
    }
}