package com.dev.factory;

import com.dev.exception.InvalidPaymentSelectionException;
import com.dev.model.CodPaymentStrategy;
import com.dev.model.UpiPaymentStrategy;
import com.dev.repository.PaymentStrategy;

import java.util.Scanner;

public class ConsolePaymentStrategyFactory implements PaymentStrategyFactory {

    @Override
    public PaymentOption createPaymentOption(int choice, Scanner scanner) {
        PaymentStrategy strategy;
        String methodName;

        switch (choice) {
            case 1:
                strategy = new UpiPaymentStrategy(scanner);
                methodName = "UPI";
                break;
            case 2:
                strategy = new CodPaymentStrategy();
                methodName = "CASH ON DELIVERY";
                break;
            default:
                throw new InvalidPaymentSelectionException("Invalid payment selection.");
        }
        return new PaymentOption(strategy, methodName);
    }
}