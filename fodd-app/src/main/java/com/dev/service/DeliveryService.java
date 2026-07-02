package com.dev.service;

import com.dev.exception.ApplicationNotFoundException;
import com.dev.exception.DuplicateApplicationException;
import com.dev.exception.UsernameTakenException;
import com.dev.model.DeliveryApplication;
import com.dev.model.DeliveryGuy;
import com.dev.repository.DeliveryApplicationRepository;
import com.dev.repository.DeliveryGuyRepository;

import java.util.List;
import java.util.Optional;

public class DeliveryService {

    private final DeliveryApplicationRepository applicationRepository;
    private final DeliveryGuyRepository deliveryGuyRepository;

    public DeliveryService(DeliveryApplicationRepository applicationRepository,
                           DeliveryGuyRepository deliveryGuyRepository) {
        this.applicationRepository = applicationRepository;
        this.deliveryGuyRepository = deliveryGuyRepository;
    }

    public boolean isDuplicateApplication(String phone)
    {
        return applicationRepository.existsByPhone(phone)
                || deliveryGuyRepository.findAll().stream().anyMatch(g -> g.getPhone().equals(phone));
    }

    public DeliveryApplication submitApplication(String name, String phone) {
        if (isDuplicateApplication(phone)) {
            throw new DuplicateApplicationException("An application with this phone number already exists!");
        }
        DeliveryApplication app = new DeliveryApplication(applicationRepository.nextId(), name, phone);
        return applicationRepository.save(app);
    }

    public List<DeliveryApplication> getPendingApplications() {
        return applicationRepository.findAll();
    }

    public boolean isUsernameTaken(String username) {
        return deliveryGuyRepository.findByUsername(username).isPresent();
    }

    public DeliveryApplication getApplicationOrThrow(int applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application ID not found."));
    }

    public DeliveryGuy approveApplication(int applicationId, String username, String password) {
        DeliveryApplication app = getApplicationOrThrow(applicationId);

        if (isUsernameTaken(username)) {
            throw new UsernameTakenException("Username is already taken.");
        }

        DeliveryGuy guy = new DeliveryGuy.Builder()
                .id(deliveryGuyRepository.nextId())
                .name(app.getName())
                .username(username)
                .password(password)
                .phone(app.getPhone())
                .build();

        deliveryGuyRepository.save(guy);
        deliveryGuyRepository.linkApplicationId(app.getApplicationId(), guy);
        applicationRepository.remove(app);  //after approved someone...now it is removed from pending list...
        return guy;
    }

    public DeliveryGuy retrieveCredentials(int applicationId) {
        Optional<DeliveryGuy> guyOpt = deliveryGuyRepository.findByApplicationId(applicationId);
        if (guyOpt.isPresent())
        {
            return guyOpt.get();
        }

        getApplicationOrThrow(applicationId);
        return null;
    }

    public Optional<DeliveryGuy> authenticate(String username, String password) {
        return deliveryGuyRepository.findByUsername(username)
                .filter(g -> g.authenticate(username, password));
    }
}