package com.habit.tracker.entry.dto;

import com.habit.tracker.entry.HabitEntryStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class HabitEntryResponse {

    private Long id;
    private Long habitId;
    private LocalDate date;
    private HabitEntryStatus status;
    private Integer value;
    private String notes;
}

