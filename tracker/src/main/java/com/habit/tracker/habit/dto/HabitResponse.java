package com.habit.tracker.habit.dto;

import com.habit.tracker.habit.HabitFrequency;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitResponse {

    private Long id;
    private String name;
    private String description;
    private HabitFrequency frequency;
    private Integer targetCount;
    private Boolean active;
}

