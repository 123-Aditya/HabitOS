package com.habit.tracker.streak.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitStreakResponse {

    private Long habitId;
    private int currentStreak;
    private int longestStreak;
}

