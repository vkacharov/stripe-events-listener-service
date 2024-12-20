package com.saintolivetree.stripe_events_listener_service.service.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DefaultMetricsService implements MetricsService {

    Logger logger = LoggerFactory.getLogger(DefaultMetricsService.class);

    @Override
    public void incrementMetric(String metric) {
        logger.info("{} increased", metric);
    }
}
