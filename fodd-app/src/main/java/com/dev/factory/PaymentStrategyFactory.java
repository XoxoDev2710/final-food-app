package com.dev.factory;

import java.util.Scanner;

public interface PaymentStrategyFactory
{
    PaymentOption createPaymentOption(int choice, Scanner scanner);
}