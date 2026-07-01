package com.dev.model;


public class Customer extends BaseUser {

    private String address;

    public Customer(int id, String name, String username, String password, String address)
    {
        super(id, name, username, password);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", username='" + username + "', address='" + address + "'}";
    }
}

