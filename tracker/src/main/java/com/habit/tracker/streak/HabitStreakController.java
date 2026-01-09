package com.habit.tracker.streak;

import com.habit.tracker.habit.Habit;
import com.habit.tracker.habit.HabitRepository;
import com.habit.tracker.streak.dto.HabitStreakResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class HabitStreakController {

    private final HabitRepository habitRepository;
    private final HabitStreakService streakService;

    @GetMapping("/{habitId}")
    public HabitStreakResponse getStreak(@PathVariable Long habitId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (!habit.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        HabitStreak streak = streakService.calculateStreak(habit);

        return HabitStreakResponse.builder()
                .habitId(habitId)
                .currentStreak(streak.getCurrentStreak())
                .longestStreak(streak.getLongestStreak())
                .build();
    }
}

