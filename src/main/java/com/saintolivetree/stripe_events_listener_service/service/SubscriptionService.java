package com.saintolivetree.stripe_events_listener_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private DonorNotificationStatusService donorNotificationStatusService;

    @Autowired
    private EncryptionService encryptionService;

    public void unsubscribe(String encryptedDonorId) {
        String donorId = encryptionService.decrypt(encryptedDonorId);
        donorNotificationStatusService.unsubscribe(donorId);
    }

    public String createUnsubscribeUrl(String donorId) {
        String encryptedDonorId = encryptionService.encrypt(donorId);
        return String.format(
                "http://localhost:8080/unsubscribe?d=%s",
                encryptedDonorId);
    }
}
