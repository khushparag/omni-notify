package com.omninotify.notification.service;

import com.omninotify.notification.model.NotificationRequest;
import com.omninotify.notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    
    private final NotificationRepository repository;

    public NotificationConsumer(NotificationRepository repository) {
        this.repository = repository;
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, multiplier = 2.0))
    @KafkaListener(topics = "notifications.sms", groupId = "notification-group")
    public void handleSms(NotificationRequest request) {
        process(request);
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, multiplier = 2.0))
    @KafkaListener(topics = "notifications.otp", groupId = "notification-group", concurrency = "3")
    public void handleOtp(NotificationRequest request) {
        process(request);
    }

    private void process(NotificationRequest request) {
        // Here we would call Twilio Rate-limit manager / 3rd party APIs
        request.setStatus("SENT");
        repository.save(request);
        System.out.println("Dispatched: " + request.getType() + " to " + request.getRecipient());
    }
}
