package com.omninotify.subscription.service;

import com.omninotify.subscription.model.UserSubscription;
import com.omninotify.subscription.repository.SubscriptionRepository;
import com.razorpay.Customer;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Subscription;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentGatewayService {

    private final RazorpayClient razorpayClient;
    private final SubscriptionRepository repository;

    public PaymentGatewayService(@Autowired(required = false) RazorpayClient razorpayClient, SubscriptionRepository repository) {
        this.razorpayClient = razorpayClient;
        this.repository = repository;
    }

    public UserSubscription createSubscription(String userId, String planId) throws RazorpayException {
        String customerId = createRazorpayCustomer(userId + "@omninotify.local", "User " + userId);
        
        JSONObject subscriptionRequest = new JSONObject();
        subscriptionRequest.put("plan_id", planId);
        subscriptionRequest.put("customer_id", customerId);
        subscriptionRequest.put("total_count", 12); 
        
        Subscription subscription = null;
        if (razorpayClient != null) {
             subscription = razorpayClient.subscriptions.create(subscriptionRequest);
        }

        UserSubscription userSub = new UserSubscription();
        userSub.setUserId(userId);
        userSub.setPlanType(planId);
        userSub.setStatus("CREATED");
        userSub.setRazorpayCustomerId(customerId);
        
        if (subscription != null) {
            userSub.setRazorpaySubscriptionId(subscription.get("id"));
        } else {
            userSub.setRazorpaySubscriptionId("sub_mock_" + System.currentTimeMillis());
        }
        userSub.setCurrentTermEnd(LocalDateTime.now().plusMonths(1));
        
        return repository.save(userSub);
    }
    
    private String createRazorpayCustomer(String email, String name) throws RazorpayException {
        if (razorpayClient == null) {
            return "cust_mock_" + System.currentTimeMillis();
        }
        JSONObject customerRequest = new JSONObject();
        customerRequest.put("name", name);
        customerRequest.put("email", email);
        Customer customer = razorpayClient.customers.create(customerRequest);
        return customer.get("id");
    }
}
