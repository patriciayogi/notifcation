package com.gigplatform.notification.consumer;

import com.gigplatform.notification.dto.NotificationEvent;
import com.gigplatform.notification.service.EmailNotificationService;
import com.gigplatform.notification.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {

    private final EmailNotificationService emailService;
    private final SmsService smsService;

    public NotificationConsumer(EmailNotificationService emailService, SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @KafkaListener(topics = "notifications", groupId = "notification-service")
    public void onMessage(NotificationEvent event) {
        log.info("Received notification event: {}", event);
        if (event == null || event.getType() == null) return;

        switch (event.getType()) {
            case "EMAIL" -> emailService.sendEmailNotification(
                    event.getTo(), event.getSubject(), event.getText()
            );
            case "SMS" -> smsService.sendSms(event.getTo(), event.getText());
            default -> throw new IllegalArgumentException("Unknown notification type: " + event.getType());
        }
    }
}
