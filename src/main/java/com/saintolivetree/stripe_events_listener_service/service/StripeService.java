package com.saintolivetree.stripe_events_listener_service.service;

import com.saintolivetree.stripe_events_listener_service.exception.StripeException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private static final Logger logger = LoggerFactory.getLogger(StripeService.class);
    private final String endpointSecret;

    public StripeService(@Value("${stripe.api.key}") String stripeApiKey,
                         @Value("${stripe.endpoint.secret}") String endpointSecret) {
        Stripe.apiKey = stripeApiKey;
        this.endpointSecret = endpointSecret;
    }

    public Event resolveEvent(String payload, String sigHeader) {
        if(sigHeader != null) {
            try {
                Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
                return event;
            } catch (SignatureVerificationException s) {
                throw new StripeException("Failed to construct Stripe event.", s);
            }

        } else {
            throw new StripeException("No sigHeader found in Stripe request.");
        }
    }

    public StripeObject deserializeStripeObject(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (dataObjectDeserializer.getObject().isPresent()) {
            StripeObject stripeObject = dataObjectDeserializer.getObject().get();
            return stripeObject;
        } else {
            logger.error("StripeObject not present.");
        }

        throw new StripeException("Failed to deserialize StripeObject from event.");
    }
}
