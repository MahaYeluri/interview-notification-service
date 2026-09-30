package com.interview.management.notificationservice.controller;


import com.interview.management.notificationservice.dto.NotificationRequest;
import com.interview.management.notificationservice.dto.NotificationResponse;
import com.interview.management.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> accept(@Valid @RequestBody NotificationRequest request) {
        log.info("Received eventId={} interviewId={}", request.eventId(), request.interviewId());
        notificationService.accept(request);
        return ResponseEntity.accepted().build();           // 202
    }

    @GetMapping
    public List<NotificationResponse> list(@RequestParam(required = false) Long interviewId) {
        return notificationService.find(interviewId);
    }
}