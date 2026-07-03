package com.dev.repository;

import com.dev.model.Customer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository
{
    private final Map<String, Customer> database = new HashMap<>();
    private int idCounter = 1;

    @Override
    public Customer save(Customer customer) {
        database.put(customer.getUsername().toLowerCase(), customer);
        return customer;
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return Optional.ofNullable(database.get(username.toLowerCase()));
    }

    @Override
    public int nextId() {
        return idCounter++;
    }
}