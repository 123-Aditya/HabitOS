package com.habit.tracker.skip.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
public class CreateSkipRuleRequest {

    @NotNull
    private Long habitId;

    private DayOfWeek dayOfWeek;
    private LocalDate specificDate;
}

