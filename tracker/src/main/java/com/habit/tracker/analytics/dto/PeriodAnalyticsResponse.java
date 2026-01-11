package com.habit.tracker.analytics.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PeriodAnalyticsResponse {

    private AnalyticsPeriod period;
    private String fromDate;
    private String toDate;

    private List<HabitAnalyticsResponse> habits;
}

