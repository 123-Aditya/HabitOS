package com.habit.tracker.skip.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Builder
public class SkipRuleResponse {

    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalDate specificDate;
}

