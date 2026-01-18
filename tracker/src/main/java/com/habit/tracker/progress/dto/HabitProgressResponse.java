package com.habit.tracker.progress.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HabitProgressResponse {

    private Long habitId;
    private String habitName;
    private String startDate;
    private String endDate;
    private List<DailyProgressResponse> progress;
}

