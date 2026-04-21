package com.omninotify.notification.service;

import com.omninotify.notification.model.NotificationRequest;
import com.omninotify.notification.repository.NotificationRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;
    private final NotificationRepository repository;

    public NotificationProducer(KafkaTemplate<String, NotificationRequest> kafkaTemplate, NotificationRepository repository) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    public void queueNotification(NotificationRequest request) {
        request.setStatus("QUEUED");
        NotificationRequest saved = repository.save(request);
        String topic = "notifications." + request.getType().toLowerCase();
        kafkaTemplate.send(topic, saved.getId().toString(), saved);
    }
}
