package com.fitmannation.controller;

import com.fitmannation.dto.AnalyticsEventRequest;
import com.fitmannation.model.AnalyticsEvent;
import com.fitmannation.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://mahesh-molugu.github.io"})
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/track")
    public ResponseEntity<Map<String, Object>> trackEvent(
            @Valid @RequestBody AnalyticsEventRequest request,
            HttpServletRequest httpRequest) {
        
        AnalyticsEvent event = analyticsService.trackEvent(
                request.getEventCategory(),
                request.getEventAction(),
                request.getEventLabel(),
                request.getUserId(),
                request.getSessionId(),
                httpRequest
        );

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("eventId", event.getId());
        response.put("message", "Event tracked successfully");

        return ResponseEntity.ok(response);
    }
}



