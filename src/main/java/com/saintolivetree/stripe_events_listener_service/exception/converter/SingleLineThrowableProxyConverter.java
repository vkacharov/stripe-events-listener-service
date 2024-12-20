package com.saintolivetree.stripe_events_listener_service.exception.converter;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

public class SingleLineThrowableProxyConverter extends ThrowableProxyConverter {
    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        String stackTrace = super.throwableProxyToString(tp);
        String replaced = stackTrace.replace("\r", "").replace("\n", " | ");
        return replaced;
    }
}
