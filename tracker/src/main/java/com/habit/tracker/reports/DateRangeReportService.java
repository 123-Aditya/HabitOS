package com.habit.tracker.reports;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.habit.tracker.analytics.ExpectedCountCalculator;
import com.habit.tracker.entry.HabitEntry;
import com.habit.tracker.entry.HabitEntryRepository;
import com.habit.tracker.exception.ResourceNotFoundException;
import com.habit.tracker.habit.Habit;
import com.habit.tracker.habit.HabitRepository;
import com.habit.tracker.reports.dto.DateRangeReportResponse;
import com.habit.tracker.reports.dto.HabitSummaryResponse;
import com.habit.tracker.streak.HabitStreak;
import com.habit.tracker.streak.HabitStreakService;
import com.habit.tracker.user.User;
import com.habit.tracker.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DateRangeReportService {

    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final HabitEntryRepository habitEntryRepository;
    private final HabitStreakService habitStreakService;
    private final ExpectedCountCalculator expectedCountCalculator;

    public DateRangeReportResponse generateSummary(
            String email,
            String from,
            String to) {

        // 1. Parse dates
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }

        // 2. Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 3. Fetch all active habits
        List<Habit> habits =
                habitRepository.findByUserAndActiveTrue(user);

        // 4. Fetch entries in date range
        List<HabitEntry> entries =
                habitEntryRepository.findByUserAndDateBetween(
                        user, startDate, endDate);

        // 5. Group entries by habitId
        Map<Long, List<HabitEntry>> entriesByHabit =
                entries.stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getHabit().getId()));

        // 6. Per-habit summaries
        List<HabitSummaryResponse> habitSummaries = new ArrayList<>();

        int totalExpected = 0;
        int totalCompleted = 0;

        for (Habit habit : habits) {

            List<HabitEntry> habitEntries =
                    entriesByHabit.getOrDefault(
                            habit.getId(),
                            Collections.emptyList());

            int completedCount = habitEntries.size();

            int expectedCount =
                    expectedCountCalculator.calculateExpected(
                            habit, startDate, endDate, user);

            double completionPercentage =
                    expectedCount == 0
                            ? 0
                            : (completedCount * 100.0) / expectedCount;

            HabitStreak streak =
                    habitStreakService.calculateStreak(
                            habit);

            habitSummaries.add(
                    HabitSummaryResponse.builder()
                            .habitName(habit.getName())
                            .frequency(habit.getFrequency())
                            .target(habit.getTargetCount())
                            .expectedCount(expectedCount)
                            .completedCount(completedCount)
                            .completionPercentage(
                                    Math.round(
                                            completionPercentage * 100.0) / 100.0)
                            .currentStreak(streak.getCurrentStreak())
                            .build()
            );

            totalExpected += expectedCount;
            totalCompleted += completedCount;
        }

        // 7. Overall completion
        double overallCompletion =
                totalExpected == 0
                        ? 0
                        : (totalCompleted * 100.0) / totalExpected;

        // 8. Final response
        return DateRangeReportResponse.builder()
                .from(startDate)
                .to(endDate)
                .totalDays(
                        ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .totalHabits(habits.size())
                .overallCompletionPercentage(
                        Math.round(overallCompletion * 100.0) / 100.0)
                .habitSummaries(habitSummaries)
                .build();
    }
}
