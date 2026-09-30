package com.interview.management.notificationservice.dto;


import com.interview.management.notificationservice.enums.Channel;
import com.interview.management.notificationservice.enums.EventType;
import com.interview.management.notificationservice.enums.NotificationStatus;
import com.interview.management.notificationservice.enums.RecipientType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id, String eventId, Long interviewId, RecipientType recipientType,
        String recipientEmail, String subject, String body, NotificationStatus status,
        int attempts, String lastError, LocalDateTime createdAt, LocalDateTime sentAt) {}
