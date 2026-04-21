package com.omninotify.subscription.repository;

import com.omninotify.subscription.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<UserSubscription, UUID> {
    Optional<UserSubscription> findByRazorpaySubscriptionId(String razorpaySubscriptionId);
    Optional<UserSubscription> findByUserId(String userId);
}
