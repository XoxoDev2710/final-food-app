package com.dev.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart
{
    public static final int MAX_QTY_PER_ITEM = 10;

    private final Map<Integer, Integer> items = new LinkedHashMap<>();

    public boolean addItem(int foodId, int quantity) {
        int existing = items.getOrDefault(foodId, 0);
        int updated = existing + quantity;
        if (updated > MAX_QTY_PER_ITEM) {
            return false;
        }
        items.put(foodId, updated);
        return true;
    }

    public void removeItem(int foodId) {
        items.remove(foodId);
    }

    public void clearCart() {
        items.clear();
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

