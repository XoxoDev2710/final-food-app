package com.dev.repository;

import com.dev.model.FoodItem;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepository {

    FoodItem save(FoodItem foodItem);

    Optional<FoodItem> findById(int foodId);

    List<FoodItem> findAll();

    int nextId();
}