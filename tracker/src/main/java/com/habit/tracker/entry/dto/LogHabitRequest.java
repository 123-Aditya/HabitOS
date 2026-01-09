package com.habit.tracker.entry.dto;

import com.habit.tracker.entry.HabitEntryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LogHabitRequest {

    @NotNull
    private Long habitId;

    @NotNull
    private LocalDate date;

    @NotNull
    private HabitEntryStatus status;

    private Integer value;
    private String notes;
}
