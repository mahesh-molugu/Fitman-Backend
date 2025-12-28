package com.fitmannation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnalyticsEventRequest {
    @NotBlank(message = "Event category is required")
    private String eventCategory;
    
    @NotBlank(message = "Event action is required")
    private String eventAction;
    
    private String eventLabel;
    private String userId;
    private String sessionId;
}


