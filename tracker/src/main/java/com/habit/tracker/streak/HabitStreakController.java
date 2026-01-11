package com.habit.tracker.streak;

import com.habit.tracker.habit.Habit;
import com.habit.tracker.habit.HabitRepository;
import com.habit.tracker.streak.dto.HabitStreakResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class HabitStreakController {

    private final HabitRepository habitRepository;
    private final HabitStreakService streakService;
    
    private static final Logger LOG = LoggerFactory.getLogger(HabitStreakController.class);

    @GetMapping("/{habitId}")
    public HabitStreakResponse getStreak(@PathVariable Long habitId) {
    	
    	LOG.info("Fetching habit with id: {}...", habitId);

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

