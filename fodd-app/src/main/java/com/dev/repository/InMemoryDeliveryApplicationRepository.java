package com.dev.repository;

import com.dev.model.DeliveryApplication;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class InMemoryDeliveryApplicationRepository implements DeliveryApplicationRepository
{
    private static final int ID_LOWER_BOUND = 100000;
    private static final int ID_UPPER_BOUND = 999999;

    private final List<DeliveryApplication> applications = new ArrayList<>();

    private final Set<Integer> usedApplicationIds = new HashSet<>();
    private final Random random = new Random();

    @Override
    public DeliveryApplication save(DeliveryApplication app) {
        applications.add(app);
        return app;
    }

    @Override
    public List<DeliveryApplication> findAll() {
        return applications;
    }

    @Override
    public Optional<DeliveryApplication> findById(int applicationId) {
        return applications.stream()
                .filter(a -> a.getApplicationId() == applicationId)
                .findFirst();
    }

    @Override
    public void remove(DeliveryApplication app) {
        applications.remove(app);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return applications.stream().anyMatch(a -> a.getPhone().equals(phone));
    }

    @Override
    public int nextId() {
        int id;
        do {
            id = ID_LOWER_BOUND + random.nextInt(ID_UPPER_BOUND - ID_LOWER_BOUND + 1);
        } while (!usedApplicationIds.add(id));
        return id;
    }
}