package com.habit.tracker.habit.dto;

import com.habit.tracker.habit.HabitFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateHabitRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private HabitFrequency frequency;

    @NotNull
    private Integer targetCount;
}

