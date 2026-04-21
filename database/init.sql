-- Omni-Notify Notification Service Schema

CREATE TABLE IF NOT EXISTS notification_requests (
    id UUID PRIMARY KEY,
    recipient VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    content TEXT,
    status VARCHAR(50),
    provider_message_id VARCHAR(255),
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Omni-Notify Subscription Service Schema

CREATE TABLE IF NOT EXISTS user_subscriptions (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    razorpay_customer_id VARCHAR(255) UNIQUE,
    razorpay_subscription_id VARCHAR(255) UNIQUE,
    plan_type VARCHAR(100),
    status VARCHAR(50),
    current_term_end TIMESTAMP
);
