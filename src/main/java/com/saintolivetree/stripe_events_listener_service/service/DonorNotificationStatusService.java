package com.saintolivetree.stripe_events_listener_service.service;

import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import com.saintolivetree.stripe_events_listener_service.repository.DonorNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DonorNotificationStatusService {
    @Autowired
    DonorNotificationRepository donorNotificationRepository;

    public void unsubscribe(String donorId) {
        donorNotificationRepository.save(new DonorNotification(donorId,
                DonorNotification.NotificationStatus.DISABLED));
    }

    public Optional<DonorNotification.NotificationStatus> getNotificationStatus(String donorId) {
        DonorNotification donorNotification = donorNotificationRepository.findById(donorId);
        if (null != donorNotification) {
            return Optional.of(donorNotification.getStatus());
        }

        return Optional.empty();
    }
}
