package com.habit.tracker.analytics;

import com.habit.tracker.analytics.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/weekly")
    public PeriodAnalyticsResponse weekly() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return analyticsService.getAnalytics(email, AnalyticsPeriod.WEEKLY);
    }

    @GetMapping("/monthly")
    public PeriodAnalyticsResponse monthly() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return analyticsService.getAnalytics(email, AnalyticsPeriod.MONTHLY);
    }
}
