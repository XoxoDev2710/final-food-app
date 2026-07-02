package com.dev.repository;

import com.dev.model.FoodItem;
import java.util.*;

public class InMemoryFoodItemRepository implements FoodItemRepository
{
    private final Map<Integer, FoodItem> database = new HashMap<>();
    private int idCounter = 101;

    @Override
    public FoodItem save(FoodItem foodItem) {
        database.put(foodItem.getFoodId(), foodItem);
        return foodItem;
    }

    @Override
    public Optional<FoodItem> findById(int foodId) {
        return Optional.ofNullable(database.get(foodId));
    }

    @Override
    public List<FoodItem> findAll() {
        List<FoodItem> all = new ArrayList<>(database.values());
        all.sort(Comparator.comparingInt(FoodItem::getFoodId));
        return all;
    }

    @Override
    public int nextId() {
        return idCounter++;
    }
}

