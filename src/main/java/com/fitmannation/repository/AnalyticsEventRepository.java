package com.fitmannation.repository;

import com.fitmannation.model.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {
    List<AnalyticsEvent> findByEventCategory(String category);
    List<AnalyticsEvent> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    long countByEventCategoryAndEventAction(String category, String action);
}


