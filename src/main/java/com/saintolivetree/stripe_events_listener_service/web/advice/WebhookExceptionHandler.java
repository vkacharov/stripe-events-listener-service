package com.saintolivetree.stripe_events_listener_service.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebhookExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebhookExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        //TODO log metric based on the exception class
        logger.error("Exception caught", e);
        return "Възникна проблем при обработката на заявката. Моля, опитайте отново.";

    }
}
