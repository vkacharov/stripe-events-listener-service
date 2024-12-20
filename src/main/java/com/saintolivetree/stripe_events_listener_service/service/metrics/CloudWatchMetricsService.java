package com.saintolivetree.stripe_events_listener_service.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class CloudWatchMetricsService implements MetricsService {
    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void incrementMetric(String metric) {
        Counter counter = Counter
                .builder(metric)
                .register(meterRegistry);
        counter.increment();
    }
}
