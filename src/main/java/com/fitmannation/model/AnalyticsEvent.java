package com.fitmannation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String eventCategory;
    
    @Column(nullable = false)
    private String eventAction;
    
    private String eventLabel;
    
    private String userId;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    
    @CreationTimestamp
    private LocalDateTime timestamp;
}



