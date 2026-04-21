package com.omninotify.notification.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class TwilioConfig {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @PostConstruct
    public void init() {
        if (!"default".equals(accountSid) && accountSid != null && !accountSid.isEmpty()) {
            Twilio.init(accountSid, authToken);
        }
    }
}
