package com.omninotify.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_requests")
public class NotificationRequest {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private UUID userId;
    private String type;
    private String recipient;
    private String content;
    private String status;
    private String providerMessageId;
    private String errorMessage;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getProviderMessageId() { return providerMessageId; }
    public void setProviderMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
