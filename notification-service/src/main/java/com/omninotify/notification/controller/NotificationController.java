package com.omninotify.notification.controller;

import com.omninotify.notification.model.NotificationRequest;
import com.omninotify.notification.service.NotificationProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notify")
public class NotificationController {
    
    private final NotificationProducer producer;
    
    public NotificationController(NotificationProducer producer) { 
        this.producer = producer; 
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        producer.queueNotification(request);
        return ResponseEntity.accepted().body("Queued");
    }
}
