package com.habit.tracker.analytics;

import com.habit.tracker.analytics.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/weekly")
    public PeriodAnalyticsResponse weekly() {
    	log.info("Fetching weekly analysis...");
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return analyticsService.getAnalytics(email, AnalyticsPeriod.WEEKLY);
    }

    @GetMapping("/monthly")
    public PeriodAnalyticsResponse monthly() {
    	log.info("Fetching monthly analysis...");
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return analyticsService.getAnalytics(email, AnalyticsPeriod.MONTHLY);
    }
}
