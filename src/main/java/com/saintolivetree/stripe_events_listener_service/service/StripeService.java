package com.saintolivetree.stripe_events_listener_service.service;

import com.google.gson.JsonSyntaxException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public Event resolveEvent(String payload) {
        try {
            Event event = ApiResource.GSON.fromJson(payload, Event.class);
            return event;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
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
