package com.saintolivetree.stripe_events_listener_service.handler;

import com.saintolivetree.stripe_events_listener_service.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class StripeEventHandler {
    @Autowired
    private StripeService stripeService;

    public abstract String getEventType();

    public void handleEvent(Event event) throws Exception {
        StripeObject stripeObject = stripeService.deserializeStripeObject(event);
        handleStripeObject(stripeObject);
    }

    protected abstract void handleStripeObject(StripeObject stripeObject) throws Exception;
}
