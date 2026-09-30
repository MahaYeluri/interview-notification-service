package com.interview.management.notificationservice.sender;

public interface EmailSender {
    void send(String to, String subject, String body);
}