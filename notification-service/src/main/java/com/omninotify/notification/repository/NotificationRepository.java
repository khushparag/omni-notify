package com.omninotify.notification.repository;

import com.omninotify.notification.model.NotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationRequest, UUID> {
}
