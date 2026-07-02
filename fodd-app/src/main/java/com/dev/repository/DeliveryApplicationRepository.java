package com.dev.repository;

import com.dev.model.DeliveryApplication;

import java.util.List;
import java.util.Optional;

public interface DeliveryApplicationRepository {

    DeliveryApplication save(DeliveryApplication app);

    List<DeliveryApplication> findAll();

    Optional<DeliveryApplication> findById(int applicationId);

    void remove(DeliveryApplication app);

    boolean existsByPhone(String phone);

    int nextId();
}