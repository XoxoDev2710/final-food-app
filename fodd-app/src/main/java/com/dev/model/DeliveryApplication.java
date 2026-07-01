package com.dev.model;

public class DeliveryApplication {

    private final int applicationId;
    private final String name;
    private final String phone;

    public DeliveryApplication(int applicationId, String name, String phone)
    {
        this.applicationId = applicationId;
        this.name = name;
        this.phone = phone;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Application #" + applicationId + " | " + name + " | " + phone;
    }
}

