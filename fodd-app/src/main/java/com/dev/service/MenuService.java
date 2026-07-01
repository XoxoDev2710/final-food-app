package com.dev.service;

import com.dev.enumm.FoodCategory;
import com.dev.exception.ItemNotFoundException;
import com.dev.model.FoodItem;
import com.dev.repository.FoodItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuService {

    private final FoodItemRepository repository;

    public MenuService(FoodItemRepository repository) {
        this.repository = repository;
    }

    public FoodItem addFoodItem(String name, double price, FoodCategory category)
    {
        FoodItem item = new FoodItem(repository.nextId(), name, price, category, true);
        return repository.save(item);
    }

    public boolean updateFoodItem(int foodId, String name, double price, FoodCategory category) {
        FoodItem item = repository.findById(foodId).orElseThrow(() -> new ItemNotFoundException("Food ID not found."));
        item.setFoodName(name);
        item.setPrice(price);
        item.setCategory(category);
        repository.save(item);
        return true;
    }

    public boolean setAvailability(int foodId, boolean available)
    {
        FoodItem item = repository.findById(foodId).orElseThrow(() -> new ItemNotFoundException("Food ID not found."));

        if (item.isAvailable() == available) {
            return false;
        }
        item.setAvailable(available);
        repository.save(item);
        return true;
    }

    public Optional<FoodItem> getItemById(int foodId) {
        return repository.findById(foodId);
    }

    public List<FoodItem> getAllItems() {
        return repository.findAll();
    }

    public List<FoodItem> getAvailableItems() {
        return repository.findAll().stream()
                .filter(FoodItem::isAvailable)
                .collect(Collectors.toList());
    }
}