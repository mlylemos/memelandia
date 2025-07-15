package com.memelandia.shared.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer requestTimer;
    private final Counter businessOperationCounter;
    
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.requestCounter = Counter.builder("http_requests_total")
                .description("Total number of HTTP requests")
                .tag("service", getServiceName())
                .register(meterRegistry);
                
        this.errorCounter = Counter.builder("http_errors_total")
                .description("Total number of HTTP errors")
                .tag("service", getServiceName())
                .register(meterRegistry);
                
        this.requestTimer = Timer.builder("http_request_duration")
                .description("HTTP request duration")
                .tag("service", getServiceName())
                .register(meterRegistry);
                
        this.businessOperationCounter = Counter.builder("business_operations_total")
                .description("Total number of business operations")
                .tag("service", getServiceName())
                .register(meterRegistry);
    }
    
    public void incrementRequestCount() {
        requestCounter.increment();
    }
    
    public void incrementErrorCount() {
        errorCounter.increment();
    }
    
    public Timer.Sample startTimer() {
        return Timer.start();
    }
    
    public void recordTimer(Timer.Sample sample) {
        sample.stop(requestTimer);
    }
    
    public void incrementBusinessOperation(String operation) {
        Counter.builder("business_operations_total")
                .description("Total number of business operations")
                .tag("service", getServiceName())
                .tag("operation", operation)
                .register(meterRegistry)
                .increment();
    }
    
    private String getServiceName() {
        return System.getProperty("spring.application.name", "unknown");
    }
}
