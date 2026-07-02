package com.dev.repository;

import com.dev.model.Order;
import java.util.*;

public class InMemoryOrderRepository implements OrderRepository
{
    private final Map<Integer, Order> database = new HashMap<>();
    private int idCounter = 1001;

    @Override
    public Order save(Order order) {
        database.put(order.getOrderId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(int orderId) {
        return Optional.ofNullable(database.get(orderId));
    }

    @Override
    public List<Order> findAll() {
        List<Order> all = new ArrayList<>(database.values());
        all.sort(Comparator.comparingInt(Order::getOrderId));
        return all;
    }

    @Override
    public int nextId() {
        return idCounter++;
    }
}