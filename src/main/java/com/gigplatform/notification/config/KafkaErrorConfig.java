package com.gigplatform.notification.config;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template, AppKafkaProperties props) {
        var recoverer = new DeadLetterPublishingRecoverer(template);

        // retry N times, then send to DLT
        var backoff = new FixedBackOff(props.getRetryIntervalMs(), props.getRetryMaxAttempts());

        var handler = new DefaultErrorHandler(recoverer, backoff);

        // don't retry for these exceptions
        handler.addNotRetryableExceptions(
                SerializationException.class,
                IllegalArgumentException.class
        );

        return handler;
    }
}
