# Notification System MVP Design Specification

## Overview
The Notification System provides a robust, resilient microservice architecture for dispatching outbound communications (SMS, WhatsApp, Email, OTP) using asynchronous Kafka messaging and third-party providers.

## Architecture & Components

A strong MVP design consists of the following logical components:

### 1. API Service (Entrypoint)
- **Responsibility:** Exposes RESTful endpoints (e.g., `/api/v1/notify`) to accept notification requests from clients or other internal microservices.
- **Action:** Validates payloads and immediately hands off to the Kafka Producer.

### 2. Kafka Producer
- **Responsibility:** Publishes validated notification requests to the appropriate Kafka topics based on the channel.
- **Tech:** Uses Spring for Apache Kafka (`KafkaTemplate`) abstraction.

### 3. Channel Consumers
- **Responsibility:** Dedicated listeners for different communication channels to ensure isolation.
- **Topics:**
  - `notifications.sms`
  - `notifications.whatsapp`
  - `notifications.email`
  - `notifications.otp` (High Priority)
- **Tech:** Uses Spring `@KafkaListener`. Separate concurrency settings and consumer groups ensure bulk SMS operations do not impact urgent OTP deliveries.

### 4. Rate-Limit / Queue Manager
- **Responsibility:** Controls throughput to third-party APIs to avoid hitting provider rate limits (e.g., Twilio 429 Too Many Requests).
- **Batching:** Manages batch behavior for bulk email/SMS jobs.

### 5. Retry & Dead-Letter Queue (DLQ) Flow
- **Responsibility:** Ensures no message is permanently lost due to transient provider network or API failures.
- **Flow:** Failed messages are transparently routed to a retry topic using Spring Kafka's built-in non-blocking retry mechanisms with exponential backoff. If all retries are exhausted, the message is routed to a Dead-Letter Queue (DLQ) for manual inspection or alerts.

### 6. Status Service & Persistence
- **Responsibility:** Stores and tracks the lifecycle of every message.
- **States:** `QUEUED`, `SENT`, `FAILED`, `RETRIED`, `DELIVERED`.
- **Database Schema (PostgreSQL):**
  - **Table: `notification_requests`**
    - `id` (UUID, Primary Key)
    - `user_id` (UUID)
    - `type` (VARCHAR: `SMS`, `EMAIL`, `WHATSAPP`, `OTP`)
    - `recipient` (VARCHAR: Phone or email)
    - `content` (TEXT)
    - `status` (VARCHAR: Enum of states)
    - `provider_message_id` (VARCHAR: Third-party ID for tracing)
    - `error_message` (TEXT)
    - `created_at`, `updated_at` (TIMESTAMP)

## Third-Party Integrations
- **SMS, WhatsApp & OTP:** Twilio API
- **Email:** Twilio SendGrid API
- Provider Webhooks will call the API Service endpoints to asynchronously update final delivery states (`DELIVERED`, `FAILED`) in the Status Service.
