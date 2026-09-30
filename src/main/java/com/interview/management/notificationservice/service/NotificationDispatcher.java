package com.interview.management.notificationservice.service;

import com.interview.management.notificationservice.entities.Notification;

import com.interview.management.notificationservice.enums.NotificationStatus;

import com.interview.management.notificationservice.repository.NotificationRepository;

import com.interview.management.notificationservice.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationDispatcher {

    private static final int MAX_ATTEMPTS = 3;

    private final NotificationRepository repository;
    private final EmailSender emailSender;

    @Scheduled(fixedDelayString = "${notification.dispatch-interval-ms:3000}")
    public void sendPending() {
        for (Notification n : repository.findTop50ByStatusOrderByIdAsc(NotificationStatus.PENDING)) {
            try {
                emailSender.send(n.getRecipientEmail(), n.getSubject(), n.getBody());
                n.markSent();
            } catch (Exception ex) {
                n.markAttemptFailed(ex.getMessage(), MAX_ATTEMPTS);
                log.warn("Send failed for notification {} (attempt {})", n.getId(), n.getAttempts());
            }
            repository.save(n);
        }
    }
}

