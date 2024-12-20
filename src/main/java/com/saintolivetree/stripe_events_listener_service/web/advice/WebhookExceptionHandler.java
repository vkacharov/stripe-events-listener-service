package com.saintolivetree.stripe_events_listener_service.web.advice;

import com.saintolivetree.stripe_events_listener_service.exception.EventHandlingException;
import com.saintolivetree.stripe_events_listener_service.service.metrics.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class WebhookExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebhookExceptionHandler.class);

    @Autowired
    private MetricsService metricsService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        String errorMetricName = "error.Exception";
        if(e instanceof EventHandlingException) {
            errorMetricName = "error." + e.getClass().getSimpleName();
        }
        logger.error("Exception caught", e);
        metricsService.incrementMetric(errorMetricName);
        return "Възникна проблем при обработката на заявката. Моля, опитайте отново.";
    }
}
