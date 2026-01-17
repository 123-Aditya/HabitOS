package com.habit.tracker.analytics;

import com.habit.tracker.analytics.dto.*;
import com.habit.tracker.entry.*;
import com.habit.tracker.exception.ResourceNotFoundException;
import com.habit.tracker.habit.*;
import com.habit.tracker.skip.HabitSkipRuleRepository;
import com.habit.tracker.streak.*;
import com.habit.tracker.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final HabitEntryRepository entryRepository;
    private final HabitSkipRuleRepository skipRuleRepository;
    private final HabitStreakService streakService;

    public PeriodAnalyticsResponse getAnalytics(
            String email,
            AnalyticsPeriod period) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LocalDate end = LocalDate.now();
        LocalDate start = (period == AnalyticsPeriod.WEEKLY)
                ? end.minusDays(6)
                : end.minusDays(29);

        List<HabitAnalyticsResponse> habitAnalytics = new ArrayList<>();

        List<Habit> habits = habitRepository.findByUserAndActiveTrue(user);

        for (Habit habit : habits) {

            int done = 0, skipped = 0, missed = 0;

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

                Optional<HabitEntry> entry =
                        entryRepository.findByHabitAndEntryDate(habit, date);

                if (entry.isPresent()) {
                    if (entry.get().getStatus() == HabitEntryStatus.DONE) 
                    	done++;
                    if (entry.get().getStatus() == HabitEntryStatus.SKIPPED) 
                    	skipped++;
                } else {
                    boolean isSkipDay =
                            skipRuleRepository.existsByHabitAndDayOfWeek(
                                    habit, date.getDayOfWeek()) ||
                            skipRuleRepository.existsByHabitAndSpecificDate(
                                    habit, date);

                    if (!isSkipDay) 
                    	missed++;
                }
            }

            double consistency = (done + missed) == 0
                    ? 100.0
                    : (done * 100.0) / (done + missed);

            HabitStreak streak = streakService.calculateStreak(habit);

            habitAnalytics.add(
                    HabitAnalyticsResponse.builder()
                            .habitId(habit.getId())
                            .habitName(habit.getName())
                            .doneCount(done)
                            .skippedCount(skipped)
                            .missedCount(missed)
                            .consistencyPercentage(
                                    Math.round(consistency * 100.0) / 100.0)
                            .currentStreak(streak.getCurrentStreak())
                            .build()
            );
        }

        return PeriodAnalyticsResponse.builder()
                .period(period)
                .fromDate(start.toString())
                .toDate(end.toString())
                .habits(habitAnalytics)
                .build();
    }
}

