package com.habit.tracker.progress.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class HabitTimelineResponse {

    private Long habitId;
    private String habitName;
    private List<DailyProgressResponse> progress;
}

