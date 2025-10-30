package com.darumng.ems.auth.service;

import com.darumng.ems.auth.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.user-events}")
    private String userEventsTopic;

    
    public void publishUserCreated(User user) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "USER_CREATED");
            event.put("userId", user.getId());
            event.put("email", user.getEmail());
            event.put("firstName", user.getFirstName());
            event.put("lastName", user.getLastName());
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(userEventsTopic, user.getId().toString(), message);

            log.info("Published USER_CREATED event for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to publish USER_CREATED event", e);
        }
    }

    
    public void publishUserUpdated(User user) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "USER_UPDATED");
            event.put("userId", user.getId());
            event.put("email", user.getEmail());
            event.put("firstName", user.getFirstName());
            event.put("lastName", user.getLastName());
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(userEventsTopic, user.getId().toString(), message);

            log.info("Published USER_UPDATED event for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to publish USER_UPDATED event", e);
        }
    }
}