package com.dev.repository;

import com.dev.model.DeliveryGuy;

import java.util.List;
import java.util.Optional;

public interface DeliveryGuyRepository {

    DeliveryGuy save(DeliveryGuy guy);

    void linkApplicationId(int applicationId, DeliveryGuy guy);

    Optional<DeliveryGuy> findByApplicationId(int applicationId);

    List<DeliveryGuy> findAll();

    Optional<DeliveryGuy> findByUsername(String username);

    int nextId();
}