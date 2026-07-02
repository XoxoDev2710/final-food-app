package com.dev.repository;

import com.dev.model.Customer;

import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findByUsername(String username);

    int nextId();

}