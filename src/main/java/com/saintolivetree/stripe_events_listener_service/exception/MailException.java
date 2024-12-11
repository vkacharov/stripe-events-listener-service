package com.saintolivetree.stripe_events_listener_service.exception;

public class MailException extends EventHandlingException {
    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}
