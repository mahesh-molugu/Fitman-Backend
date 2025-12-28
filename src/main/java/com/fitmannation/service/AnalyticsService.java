package com.fitmannation.service;

import com.fitmannation.model.AnalyticsEvent;
import com.fitmannation.repository.AnalyticsEventRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventRepository analyticsEventRepository;

    @Transactional
    public AnalyticsEvent trackEvent(String category, String action, String label, 
                                    String userId, String sessionId, HttpServletRequest request) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setEventCategory(category);
        event.setEventAction(action);
        event.setEventLabel(label);
        event.setUserId(userId);
        event.setSessionId(sessionId);
        event.setIpAddress(getClientIpAddress(request));
        event.setUserAgent(request.getHeader("User-Agent"));

        return analyticsEventRepository.save(event);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}



