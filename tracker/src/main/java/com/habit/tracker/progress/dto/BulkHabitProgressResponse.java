package com.habit.tracker.progress.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class BulkHabitProgressResponse {

    private String startDate;
    private String endDate;
    private List<HabitTimelineResponse> habits;
}
