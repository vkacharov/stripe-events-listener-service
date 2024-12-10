package com.saintolivetree.stripe_events_listener_service.web.controller;

import com.saintolivetree.stripe_events_listener_service.handler.StripeEventHandler;
import com.saintolivetree.stripe_events_listener_service.model.DonorNotification;
import com.saintolivetree.stripe_events_listener_service.repository.DonorNotificationRepository;
import com.saintolivetree.stripe_events_listener_service.service.StripeService;
import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookController {
    @Autowired
    private StripeService stripeService;

    @Autowired
    private StripeEventHandler stripeEventHandler; //FIXME

    @Autowired
    private DonorNotificationRepository donorNotificationRepository;

    @PostMapping("/webhook")
    public void generatePdf(@RequestBody String payload,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            Event event = stripeService.resolveEvent(payload, request.getHeader("Stripe-Signature"));
            if (stripeEventHandler.getEventType().equals(event.getType())) {
                stripeEventHandler.handleEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

    @GetMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam(name = "d") String donorId) {
        DonorNotification donorNotification = new DonorNotification(donorId,
                DonorNotification.NotificationStatus.DISABLED);
        donorNotificationRepository.save(donorNotification);
        return ResponseEntity.noContent().build();
    }
}
