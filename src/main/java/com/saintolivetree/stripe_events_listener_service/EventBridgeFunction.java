package com.saintolivetree.stripe_events_listener_service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.saintolivetree.stripe_events_listener_service.handler.StripeEventHandler;
import com.saintolivetree.stripe_events_listener_service.service.StripeService;
import com.stripe.model.Event;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

public class EventBridgeFunction implements Consumer<String> {

    @Autowired
    private StripeEventHandler stripeEventHandler;

    @Autowired
    private StripeService stripeService;

    @Override
    public void accept(String payload) {
        try {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
            String rawEvent = jsonObject.getAsJsonObject("detail").toString();

            Event event = stripeService.resolveEvent(rawEvent);
            if (stripeEventHandler.getEventType().equals(event.getType())) {
                stripeEventHandler.handleEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}