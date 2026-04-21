package com.omninotify.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omninotify.notification.model.NotificationRequest;
import com.omninotify.notification.service.NotificationProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationProducer producer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldQueueNotification() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setType("SMS");
        request.setRecipient("+1234567890");

        doNothing().when(producer).queueNotification(any(NotificationRequest.class));

        mockMvc.perform(post("/api/v1/notify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Queued"));
    }
}
