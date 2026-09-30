package com.interview.management.notificationservice.entities;


import com.interview.management.notificationservice.enums.Channel;
import com.interview.management.notificationservice.enums.EventType;
import com.interview.management.notificationservice.enums.NotificationStatus;
import com.interview.management.notificationservice.enums.RecipientType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    @Enumerated(EnumType.STRING) private EventType eventType;
    private Long interviewId;

    @Enumerated(EnumType.STRING) private RecipientType recipientType;
    private String recipientName;
    private String recipientEmail;
    @Enumerated(EnumType.STRING) private Channel channel;

    private String subject;
    private String body;

    @Enumerated(EnumType.STRING) private NotificationStatus status;
    private int attempts;
    private String lastError;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public static Notification pending(String eventId, EventType type, Long interviewId,
                                       RecipientType recipientType, String name, String email,
                                       String subject, String body) {
        Notification n = new Notification();
        n.eventId = eventId;
        n.eventType = type;
        n.interviewId = interviewId;
        n.recipientType = recipientType;
        n.recipientName = name;
        n.recipientEmail = email;
        n.channel = Channel.EMAIL;
        n.subject = subject;
        n.body = body;
        n.status = NotificationStatus.PENDING;
        n.createdAt = LocalDateTime.now();
        return n;
    }

    public void markSent() {
        status = NotificationStatus.SENT;
        sentAt = LocalDateTime.now();
        lastError = null;
    }

    public void markAttemptFailed(String error, int maxAttempts) {
        attempts++;
        lastError = error == null ? null : error.substring(0, Math.min(error.length(), 1000));
        if (attempts >= maxAttempts) status = NotificationStatus.FAILED;   // otherwise stays PENDING
    }
}