package com.dev.repository;

import com.dev.model.DeliveryGuy;
import java.util.*;

public class InMemoryDeliveryGuyRepository implements DeliveryGuyRepository
{

    private final Map<String, DeliveryGuy> usernameMap = new HashMap<>();
    private final Map<Integer, DeliveryGuy> applicationIdMap = new HashMap<>();
    private int idCounter = 1;

    @Override
    public DeliveryGuy save(DeliveryGuy guy) {
        usernameMap.put(guy.getUsername().toLowerCase(), guy);
        return guy;
    }

    @Override
    public void linkApplicationId(int applicationId, DeliveryGuy guy) {
        applicationIdMap.put(applicationId, guy);
    }

    @Override
    public Optional<DeliveryGuy> findByApplicationId(int applicationId) {
        return Optional.ofNullable(applicationIdMap.get(applicationId));
    }

    @Override
    public List<DeliveryGuy> findAll() {
        return new ArrayList<>(usernameMap.values());
    }

    @Override
    public Optional<DeliveryGuy> findByUsername(String username) {
        return Optional.ofNullable(usernameMap.get(username.toLowerCase()));
    }

    @Override
    public int nextId() {
        return idCounter++;
    }
}