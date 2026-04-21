package com.omninotify.subscription.controller;

import com.omninotify.subscription.model.UserSubscription;
import com.omninotify.subscription.service.PaymentGatewayService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final PaymentGatewayService paymentService;

    @Value("${razorpay.webhook-secret:default}")
    private String webhookSecret;

    public SubscriptionController(PaymentGatewayService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserSubscription> createSubscription(@RequestParam String userId, @RequestParam String planId) {
        try {
            UserSubscription sub = paymentService.createSubscription(userId, planId);
            return ResponseEntity.ok(sub);
        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        try {
            if (!"default".equals(webhookSecret) && signature != null) {
                boolean isValid = Utils.verifyWebhookSignature(payload, signature, webhookSecret);
                if (!isValid) {
                    return ResponseEntity.status(403).body("Invalid Signature");
                }
            }
            
            JSONObject event = new JSONObject(payload);
            String eventType = event.getString("event");
            
            if ("subscription.charged".equals(eventType) || "subscription.activated".equals(eventType)) {
                JSONObject subscriptionPayload = event.getJSONObject("payload").getJSONObject("subscription").getJSONObject("entity");
                String subId = subscriptionPayload.getString("id");
                
                System.out.println("Webhook validated successfully: Subscription event '" + eventType + "' for " + subId + " recorded!");
            }
            return ResponseEntity.ok("Received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing webhook");
        }
    }
}
