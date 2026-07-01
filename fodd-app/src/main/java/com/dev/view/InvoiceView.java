package com.dev.view;

import com.dev.model.Cart;
import com.dev.model.FoodItem;
import com.dev.service.MenuService;

import java.util.Map;
import java.util.Optional;

public class InvoiceView {

    private static final String DIVIDER = "-------------------------------------------------------------";
    private static final String ROW_FORMAT = "%-6s %-25s %-6s %12s%n";

    public static void printCartInvoice(Cart cart, MenuService menuService, double subtotal,
                                        double discount, double grandTotal) {
        System.out.println("\n" + DIVIDER);
        System.out.println("                         YOUR CART (INVOICE)");
        System.out.println(DIVIDER);
        System.out.printf(ROW_FORMAT, "ID", "ITEM", "QTY", "AMOUNT");
        System.out.println(DIVIDER);

        for (Map.Entry<Integer, Integer> entry : cart.getItems().entrySet()) {
            Optional<FoodItem> itemOpt = menuService.getItemById(entry.getKey());
            if (itemOpt.isEmpty()) continue;

            FoodItem item = itemOpt.get();
            double lineTotal = item.getPrice() * entry.getValue();
            System.out.printf(ROW_FORMAT, item.getFoodId(), truncate(item.getFoodName(), 25),
                    entry.getValue(), String.format("\u20B9%.2f", lineTotal));
        }

        System.out.println(DIVIDER);
        printSummaryLine("Subtotal", subtotal, false);
        if (discount > 0) {
            printSummaryLine("Discount", discount, true);
        }
        System.out.println(DIVIDER);
        printSummaryLine("GRAND TOTAL", grandTotal, false);
        System.out.println(DIVIDER);
    }

    private static void printSummaryLine(String label, double amount, boolean isDeduction) {
        String sign = isDeduction ? "-" : "";
        System.out.printf("%-38s %12s%n", label, sign + "\u20B9" + String.format("%.2f", amount));
    }

    private static String truncate(String value, int maxLen) {
        return value.length() > maxLen ? value.substring(0, maxLen - 3) + "..." : value;
    }
}