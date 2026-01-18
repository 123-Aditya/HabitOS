package com.habit.tracker.progress.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyProgressResponse {
    private String date;
    private ProgressStatus status;
}

