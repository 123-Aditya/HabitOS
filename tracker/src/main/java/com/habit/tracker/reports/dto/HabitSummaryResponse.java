package com.habit.tracker.reports.dto;

import com.habit.tracker.habit.HabitFrequency;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HabitSummaryResponse {
    private String habitName;
    private HabitFrequency frequency;
    private int target;
    private int expectedCount;
    private int completedCount;
    private double completionPercentage;
    private int currentStreak;
}

