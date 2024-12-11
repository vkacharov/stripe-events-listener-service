package com.saintolivetree.stripe_events_listener_service.exception;

public class TemplateException extends EventHandlingException {
    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
