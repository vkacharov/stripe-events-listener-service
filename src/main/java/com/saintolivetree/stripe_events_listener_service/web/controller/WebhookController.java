package com.saintolivetree.stripe_events_listener_service.web.controller;

import com.saintolivetree.stripe_events_listener_service.handler.StripeEventHandler;
import com.saintolivetree.stripe_events_listener_service.service.DonorNotificationStatusService;
import com.saintolivetree.stripe_events_listener_service.service.EncryptionService;
import com.saintolivetree.stripe_events_listener_service.service.StripeService;
import com.stripe.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private DonorNotificationStatusService donorNotificationStatusService;

    @Autowired
    private EncryptionService encryptionService;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/webhook")
    public void generatePdf(@RequestBody String payload,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Event event = stripeService.resolveEvent(payload, request.getHeader("Stripe-Signature"));
        if (stripeEventHandler.getEventType().equals(event.getType())) {
            stripeEventHandler.handleEvent(event);
        }
    }

    @GetMapping("/unsubscribe")
    @ResponseBody
    public ResponseEntity<String> unsubscribe(@RequestParam(name = "d") String encryptedDonorId) {
        String donorId = encryptionService.decrypt(encryptedDonorId);
        donorNotificationStatusService.unsubscribe(donorId);
        logger.info("Donor with id {} unsubscribed.", encryptedDonorId);
        return ResponseEntity.ok().body("Бяхте успешно отписани.");
    }
}
