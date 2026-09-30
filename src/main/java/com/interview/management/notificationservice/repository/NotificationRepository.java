package com.interview.management.notificationservice.repository;


import com.interview.management.notificationservice.entities.Notification;
import com.interview.management.notificationservice.enums.Channel;
import com.interview.management.notificationservice.enums.NotificationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByEventIdAndRecipientEmailAndChannel(String eventId, String email, Channel channel);

    List<Notification> findTop50ByStatusOrderByIdAsc(NotificationStatus status);

    List<Notification> findByInterviewId(Long interviewId);
}

