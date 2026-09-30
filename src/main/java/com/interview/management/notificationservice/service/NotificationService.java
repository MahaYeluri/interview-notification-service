package com.interview.management.notificationservice.service;

import com.interview.management.notificationservice.dto.NotificationRequest;
import com.interview.management.notificationservice.dto.NotificationResponse;
import com.interview.management.notificationservice.entities.Notification;
import com.interview.management.notificationservice.enums.Channel;

import com.interview.management.notificationservice.enums.RecipientType;
import com.interview.management.notificationservice.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j

public class NotificationService {

    private final NotificationRepository repository;

    private record Message(String subject, String body) {}

    @Transactional
    public void accept(NotificationRequest request) {
        String eventId = request.eventId().toString();

        for (NotificationRequest.Recipient r : request.recipients()) {
            if (repository.existsByEventIdAndRecipientEmailAndChannel(eventId, r.email(), Channel.EMAIL)) {
                log.info("Duplicate ignored: eventId={} email={}", eventId, r.email());
                continue;                                   // idempotent: redelivery creates nothing
            }
            Message m = compose(r, request.details());
            repository.save(Notification.pending(eventId, request.eventType(), request.interviewId(),
                    r.type(), r.name(), r.email(), m.subject(), m.body()));
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> find(Long interviewId) {
        List<Notification> list = interviewId == null
                ? repository.findAll() : repository.findByInterviewId(interviewId);
        return list.stream().map(this::toResponse).toList();
    }

    private Message compose(NotificationRequest.Recipient r, Map<String, String> d) {
        String name = r.name() == null ? "there" : r.name();
        if (r.type() == RecipientType.CANDIDATE) {
            return new Message("Interview scheduled",
                    "Hi %s, your %s interview is scheduled from %s to %s. Meeting link: %s"
                            .formatted(name, d.get("round"), d.get("startTime"), d.get("endTime"), d.get("meetingLink")));
        }
        return new Message("Interview assigned",
                "Hi %s, you are on the panel for %s's %s interview from %s to %s. Meeting link: %s"
                        .formatted(name, d.get("candidateName"), d.get("round"),
                                d.get("startTime"), d.get("endTime"), d.get("meetingLink")));
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(n.getId(), n.getEventId(), n.getInterviewId(), n.getRecipientType(),
                n.getRecipientEmail(), n.getSubject(), n.getBody(), n.getStatus(),
                n.getAttempts(), n.getLastError(), n.getCreatedAt(), n.getSentAt());
    }
}