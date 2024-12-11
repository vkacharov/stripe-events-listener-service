package com.saintolivetree.stripe_events_listener_service.exception;

public class EventHandlingException extends RuntimeException {

    public EventHandlingException(String message) {
        super(message);
    }

    public EventHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
