package com.dev.seeder;

import com.dev.model.Customer;
import com.dev.model.DeliveryGuy;
import com.dev.model.FoodItem;
import com.dev.enumm.FoodCategory;
import com.dev.repository.CustomerRepository;
import com.dev.repository.DeliveryGuyRepository;
import com.dev.repository.FoodItemRepository;

public class DataSeeder {

    public static void seedData(FoodItemRepository foodItemRepo, CustomerRepository customerRepo, DeliveryGuyRepository deliveryGuyRepo) {
        
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Cheese Burger", 120.0, FoodCategory.FAST_FOOD, true));
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Masala Dosa", 80.0, FoodCategory.SOUTH_INDIAN, true));
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Butter Paneer Masala", 250.0, FoodCategory.PUNJABI, true));
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Paneer Kulcha", 40.0, FoodCategory.PUNJABI, true));
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Veg Hakka Noodles", 150.0, FoodCategory.CHINESE, true));
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Chocolate Brownie", 90.0, FoodCategory.DESSERT, true));
        foodItemRepo.save(new FoodItem(foodItemRepo.nextId(), "Mango Lassi", 60.0, FoodCategory.DRINK, true));


        customerRepo.save(new Customer(customerRepo.nextId(), "John Doe", "john.doe", "password123", "Flat 401, Elite Residency, Mumbai"));
        customerRepo.save(new Customer(customerRepo.nextId(), "Alice Smith", "alice.s", "password123", "Sector 15, Vashi, Navi Mumbai"));
        customerRepo.save(new Customer(customerRepo.nextId(), "Bob Johnson", "bob.j", "password123", "B-12, Green Glen Layout, Mumbai"));

        deliveryGuyRepo.save(new DeliveryGuy.Builder()
                .id(deliveryGuyRepo.nextId())
                .name("Ramesh Kumar")
                .username("ramesh.k")
                .password("agent123")
                .phone("9876543210")
                .build());
        
        deliveryGuyRepo.save(new DeliveryGuy.Builder()
                .id(deliveryGuyRepo.nextId())
                .name("Suresh Singh")
                .username("suresh.s")
                .password("agent123")
                .phone("9765432109")
                .build());

        deliveryGuyRepo.save(new DeliveryGuy.Builder()
                .id(deliveryGuyRepo.nextId())
                .name("Amit Sharma")
                .username("amit.s")
                .password("agent123")
                .phone("9654321098")
                .build());
    }
}
