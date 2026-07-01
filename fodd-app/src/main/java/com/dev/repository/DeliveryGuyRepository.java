package com.dev.repository;

import com.dev.model.DeliveryGuy;

import java.io.Serializable;
import java.util.*;

public class DeliveryGuyRepository {

    private final Map<String, DeliveryGuy> usernameMap = new HashMap<>();
    private final Map<Integer, DeliveryGuy> applicationIdMap = new HashMap<>();
    private int idCounter = 1;

    public DeliveryGuy save(DeliveryGuy guy) {
        usernameMap.put(guy.getUsername().toLowerCase(), guy);
        return guy;
    }

    public void linkApplicationId(int applicationId, DeliveryGuy guy) {
        applicationIdMap.put(applicationId, guy);
    }

    public Optional<DeliveryGuy> findByApplicationId(int applicationId) {
        return Optional.ofNullable(applicationIdMap.get(applicationId));
    }

    public List<DeliveryGuy> findAll() {
        return new ArrayList<>(usernameMap.values());
    }

    public Optional<DeliveryGuy> findByUsername(String username) {
        return Optional.ofNullable(usernameMap.get(username.toLowerCase()));
    }

    public int nextId() {
        return idCounter++;
    }
}