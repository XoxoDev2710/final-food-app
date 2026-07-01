package com.dev.model;

import com.dev.enumm.FoodCategory;

public class FoodItem {

    private final int foodId;
    private String foodName;
    private double price;
    private FoodCategory category;
    private boolean available;

    public FoodItem(int foodId, String foodName, double price, FoodCategory category, boolean available) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public int getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Category: %s | Price: \u20B9%.2f | Available: %b",
                foodId, foodName, category, price, available);
    }
}
