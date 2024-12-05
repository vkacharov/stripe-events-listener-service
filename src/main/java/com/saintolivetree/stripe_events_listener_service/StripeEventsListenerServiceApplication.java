package com.saintolivetree.stripe_events_listener_service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class StripeEventsListenerServiceApplication {

	public static void main(String[] args) {
	}

	@Bean
	public Consumer<String> eventBridgeFunction() {
		return new EventBridgeFunction();
	}
}
