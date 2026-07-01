package com.dev.repository;


import com.dev.model.Order;

import java.io.Serializable;
import java.util.*;

public class InMemoryOrderRepository
{

    private final Map<Integer, Order> database = new HashMap<>();
    private int idCounter = 1001;

    public Order save(Order order) {
        database.put(order.getOrderId(), order);
        return order;
    }

    public Optional<Order> findById(int orderId) {
        return Optional.ofNullable(database.get(orderId));
    }

    public List<Order> findAll() {
        List<Order> all = new ArrayList<>(database.values());
        all.sort(Comparator.comparingInt(Order::getOrderId));
        return all;
    }

    public int nextId() {
        return idCounter++;
    }
}

