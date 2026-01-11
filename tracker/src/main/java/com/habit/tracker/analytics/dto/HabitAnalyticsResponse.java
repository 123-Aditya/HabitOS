package com.habit.tracker.analytics.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitAnalyticsResponse {

    private Long habitId;
    private String habitName;

    private int doneCount;
    private int skippedCount;
    private int missedCount;

    private double consistencyPercentage;
    private int currentStreak;
}

