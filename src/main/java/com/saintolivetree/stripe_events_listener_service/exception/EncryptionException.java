package com.saintolivetree.stripe_events_listener_service.exception;

public class EncryptionException extends EventHandlingException {
    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
