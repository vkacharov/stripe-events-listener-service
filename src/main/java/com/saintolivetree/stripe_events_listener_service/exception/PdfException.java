package com.saintolivetree.stripe_events_listener_service.exception;

public class PdfException extends EventHandlingException {
    public PdfException(String message) {
        super(message);
    }

    public PdfException(String message, Throwable cause) {
        super(message, cause);
    }
}
