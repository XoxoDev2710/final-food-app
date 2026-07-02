package com.dev.repository;

import com.dev.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(int orderId);

    List<Order> findAll();

    int nextId();
}