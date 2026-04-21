package com.omninotify.notification.repository;

import com.omninotify.notification.model.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private NotificationRepository repository;

    @Test
    void shouldSaveAndRetrieve() {
        NotificationRequest request = new NotificationRequest();
        request.setType("SMS");
        request.setStatus("QUEUED");
        request.setRecipient("+1234567890");
        request.setCreatedAt(LocalDateTime.now());
        
        NotificationRequest saved = repository.save(request);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo("SMS");
        assertThat(saved.getStatus()).isEqualTo("QUEUED");
    }
}
