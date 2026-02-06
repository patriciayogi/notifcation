package com.gigplatform.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.kafka")
public class AppKafkaProperties {

    private int concurrency = 1;

    private long retryIntervalMs = 2000;
    private long retryMaxAttempts = 3;

}