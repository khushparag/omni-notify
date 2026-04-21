# Omni-Notify

Omni-Notify is a scalable, resilient notification system built using a microservice architecture. It allows sending multi-channel notifications including SMS, Email, WhatsApp, and OTPs.

## Architecture

The system is designed with multiple microservices:
- **API Gateway**: Entry point for all client requests.
- **Notification Service**: Core component that accepts notification requests, publishes them to Kafka, and consumes them using varying priority levels.
- **Subscription Service**: Manages user subscription tiers and billing using Razorpay.

## Technology Stack

- **Java 17+**
- **Spring Boot 3**.x
- **Spring Data JPA** with **PostgreSQL**
- **Apache Kafka** for asynchronous messaging and priority queuing
- **Twilio & SendGrid API** for actual message delivery
- **Docker & docker-compose** for infrastructure and deployment

## Getting Started

1. Start up the prerequisite infrastructure (Kafka and PostgreSQL).
2. Configure settings in your respective microservice `application.yml`.
3. Build and launch with `mvn clean install` and `mvn spring-boot:run`.

*(This project is currently under active development.)*
