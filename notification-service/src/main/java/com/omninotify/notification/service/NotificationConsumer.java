package com.omninotify.notification.service;

import com.omninotify.notification.model.NotificationRequest;
import com.omninotify.notification.repository.NotificationRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    
    private final NotificationRepository repository;
    
    @Value("${twilio.from-number}")
    private String fromNumber;

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
        try {
            if ("default".equals(fromNumber) || fromNumber == null) {
                System.out.println("Dispatched (Mock/Console): " + request.getType() + " to " + request.getRecipient());
                request.setStatus("SENT");
            } else {
                Message message = Message.creator(
                        new PhoneNumber(request.getRecipient()),
                        new PhoneNumber(fromNumber),
                        request.getContent() != null ? request.getContent() : "Omni-Notify Alert!"
                ).create();
                
                request.setProviderMessageId(message.getSid());
                request.setStatus("SENT");
                System.out.println("Dispatched (Twilio " + message.getSid() + "): " + request.getType() + " to " + request.getRecipient());
            }
        } catch (Exception e) {
            System.err.println("Failed to send notification via Twilio: " + e.getMessage());
            request.setStatus("FAILED");
            request.setErrorMessage(e.getMessage());
            repository.save(request);
            throw e; // throw error so the robust @RetryableTopic handles retry backoff
        }
        repository.save(request);
    }
}
