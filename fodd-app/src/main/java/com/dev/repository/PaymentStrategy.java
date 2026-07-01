package com.dev.repository;

public interface PaymentStrategy
{
    boolean processPayment(double amount);
}