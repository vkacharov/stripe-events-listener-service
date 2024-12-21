package com.saintolivetree.stripe_events_listener_service.web.controller;

import com.saintolivetree.stripe_events_listener_service.handler.StripeEventHandler;
import com.saintolivetree.stripe_events_listener_service.service.DonorNotificationStatusService;
import com.saintolivetree.stripe_events_listener_service.service.EncryptionService;
import com.saintolivetree.stripe_events_listener_service.service.StripeService;
import com.saintolivetree.stripe_events_listener_service.service.metrics.MetricsService;
import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebhookController {
    @Autowired
    private StripeService stripeService;

    @Autowired
    private StripeEventHandler stripeEventHandler; //FIXME

    @Autowired
    private DonorNotificationStatusService donorNotificationStatusService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private MetricsService metricsService;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/webhook")
    @ResponseBody
    public ResponseEntity<Void> handleStripeEvent(@RequestBody String payload,
                                            HttpServletRequest request) {
        metricsService.incrementMetric("event.StripeEventReceived");
        Event event = stripeService.resolveEvent(payload, request.getHeader("Stripe-Signature"));
        if (stripeEventHandler.getEventType().equals(event.getType())) {
            stripeEventHandler.handleEvent(event);
        } else {
            logger.error("Unknown event type received {}", event.getType());
            metricsService.incrementMetric("error.UnknownStripeEvent");
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unsubscribe")
    public String unsubscribe(@RequestParam(name = "d") String encryptedDonorId) {
        metricsService.incrementMetric("event.UnsubscribeEventReceived");
        String donorId = encryptionService.decrypt(encryptedDonorId);
        donorNotificationStatusService.unsubscribe(donorId);
        logger.info("Donor with id {} unsubscribed.", encryptedDonorId);
        metricsService.incrementMetric("event.UnsubscribeEventSucceeded");
        return "unsubscribed";
    }
}
