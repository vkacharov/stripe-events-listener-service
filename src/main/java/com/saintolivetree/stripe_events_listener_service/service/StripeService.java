package com.saintolivetree.stripe_events_listener_service.service;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private final String endpointSecret;

    public StripeService(@Value("${stripe.api.key}") String stripeApiKey,
                         @Value("${stripe.endpoint.secret}") String endpointSecret) {
        Stripe.apiKey = stripeApiKey;
        this.endpointSecret = endpointSecret;
    }

    public Event resolveEvent(String payload, String sigHeader) throws SignatureVerificationException {
        if(sigHeader != null) {
           Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
           return event;
        } else {
            throw new SignatureVerificationException("No sigHeader found in request", sigHeader);
        }
    }

    public StripeObject deserializeStripeObject(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        return stripeObject;
    }
}
