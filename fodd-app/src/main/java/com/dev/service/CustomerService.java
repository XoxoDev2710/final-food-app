package com.dev.service;


import com.dev.model.Customer;
import com.dev.repository.InMemoryCustomerRepository;

import java.util.Optional;

public class CustomerService {

    private final InMemoryCustomerRepository repository;

    public CustomerService(InMemoryCustomerRepository repository) {
        this.repository = repository;
    }

    public boolean isUsernameTaken(String username) {
        return repository.findByUsername(username).isPresent();
    }

    public boolean registerCustomer(String name, String username, String password, String address) {
        if (isUsernameTaken(username)) {
            return false;
        }
        Customer customer = new Customer(repository.nextId(), name, username, password, address);
        repository.save(customer);
        return true;
    }

    public Optional<Customer> authenticate(String username, String password) {
        return repository.findByUsername(username)
                .filter(c -> c.authenticate(username, password));
    }
}

