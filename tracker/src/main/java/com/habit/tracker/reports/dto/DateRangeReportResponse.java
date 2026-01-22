package com.habit.tracker.reports.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateRangeReportResponse {
    private LocalDate from;
    private LocalDate to;
    private long totalDays;
    private int totalHabits;
    private double overallCompletionPercentage;
    private List<HabitSummaryResponse> habitSummaries;
}
