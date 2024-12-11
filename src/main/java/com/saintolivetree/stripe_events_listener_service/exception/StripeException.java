package com.saintolivetree.stripe_events_listener_service.exception;

public class StripeException extends EventHandlingException {
    public StripeException(String message) {
        super(message);
    }

    public StripeException(String message, Throwable cause) {
        super(message, cause);
    }
}
