package com.dev.repository;


import com.dev.model.Customer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository
{
    private final Map<String, Customer> database = new HashMap<>();
    private int idCounter = 1;

    public Customer save(Customer customer) {
        database.put(customer.getUsername().toLowerCase(), customer);
        return customer;
    }

    public Optional<Customer> findByUsername(String username) {
        return Optional.ofNullable(database.get(username.toLowerCase()));
    }

    public int nextId() {
        return idCounter++;
    }
}

