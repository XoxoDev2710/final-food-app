package com.dev.repository;

import com.dev.model.DeliveryApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class DeliveryApplicationRepository
{

    private static final int ID_LOWER_BOUND = 100000;
    private static final int ID_UPPER_BOUND = 999999;

    private final List<DeliveryApplication> applications = new ArrayList<>();


    //it was just adds the unique ID's into set (userApplicationIDS) so if duplicated random ID's are generated then it will no allows
    private final Set<Integer> usedApplicationIds = new HashSet<>();
    private final Random random = new Random();

    public DeliveryApplication save(DeliveryApplication app) {
        applications.add(app);
        return app;
    }

    public List<DeliveryApplication> findAll() {
        return applications;
    }

    public Optional<DeliveryApplication> findById(int applicationId) {
        return applications.stream()
                .filter(a -> a.getApplicationId() == applicationId)
                .findFirst();
    }

    public void remove(DeliveryApplication app) {
        applications.remove(app);
    }

    public boolean existsByPhone(String phone) {
        return applications.stream().anyMatch(a -> a.getPhone().equals(phone));
    }

    public int nextId() {
        int id;
        do {
            id = ID_LOWER_BOUND + random.nextInt(ID_UPPER_BOUND - ID_LOWER_BOUND + 1);
        } while (!usedApplicationIds.add(id));
        return id;
    }
}