package com.interview.management.notificationservice.sender;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingEmailSender implements EmailSender {
    @Override
    public void send(String to, String subject, String body) {
        log.info("EMAIL >>> to={} | subject={} | body={}", to, subject, body);
    }
}