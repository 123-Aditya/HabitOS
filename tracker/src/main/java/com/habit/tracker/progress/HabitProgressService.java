package com.habit.tracker.progress;

import com.habit.tracker.entry.*;
import com.habit.tracker.habit.*;
import com.habit.tracker.progress.dto.*;
import com.habit.tracker.skip.HabitSkipRuleRepository;
import com.habit.tracker.user.*;
import com.habit.tracker.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HabitProgressService {

    private final HabitRepository habitRepository;
    private final HabitEntryRepository entryRepository;
    private final HabitSkipRuleRepository skipRuleRepository;
    private final UserRepository userRepository;

    public HabitProgressResponse getProgress(
            Long habitId,
            String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found"));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(
                    "You are not allowed to access this habit");
        }

        LocalDate start = habit.getCreatedAt().toLocalDate();
        LocalDate end = LocalDate.now();

        List<DailyProgressResponse> progressList = new ArrayList<>();

        for (LocalDate date = start;
             !date.isAfter(end);
             date = date.plusDays(1)) {

            Optional<HabitEntry> entry =
                    entryRepository.findByHabitAndEntryDate(habit, date);

            ProgressStatus status;

            if (entry.isPresent()) {
                status = (entry.get().getStatus() == HabitEntryStatus.DONE)
                        ? ProgressStatus.DONE
                        : ProgressStatus.SKIPPED;
            } else {
                boolean isSkipRule =
                        skipRuleRepository.existsByHabitAndDayOfWeek(
                                habit, date.getDayOfWeek()) ||
                        skipRuleRepository.existsByHabitAndSpecificDate(
                                habit, date);

                status = isSkipRule
                        ? ProgressStatus.SKIP_RULE
                        : ProgressStatus.MISSED;
            }

            progressList.add(
                    DailyProgressResponse.builder()
                            .date(date.toString())
                            .status(status)
                            .build()
            );
        }

        return HabitProgressResponse.builder()
                .habitId(habit.getId())
                .habitName(habit.getName())
                .startDate(start.toString())
                .endDate(end.toString())
                .progress(progressList)
                .build();
    }
    
    public HabitProgressResponse getProgressByName(
            String habitName,
            String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Habit habit = habitRepository
                .findByUserAndNameIgnoreCase(user, habitName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Habit not found with name: " + habitName));

        return getProgress(habit.getId(), userEmail);
    }
    
    
    public BulkHabitProgressResponse getBulkProgress(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Habit> habits = habitRepository.findByUserAndActiveTrue(user);

        if (habits.isEmpty()) {
            return BulkHabitProgressResponse.builder()
                    .startDate(LocalDate.now().toString())
                    .endDate(LocalDate.now().toString())
                    .habits(List.of())
                    .build();
        }

        LocalDate globalStart = habits.stream()
                .map(h -> h.getCreatedAt().toLocalDate())
                .min(LocalDate::compareTo)
                .get();

        LocalDate end = LocalDate.now();

        List<HabitTimelineResponse> timelines = new ArrayList<>();

        for (Habit habit : habits) {

            HabitProgressResponse single =
                    getProgress(habit.getId(), userEmail);

            timelines.add(
                    HabitTimelineResponse.builder()
                            .habitId(single.getHabitId())
                            .habitName(single.getHabitName())
                            .progress(single.getProgress())
                            .build()
            );
        }

        return BulkHabitProgressResponse.builder()
                .startDate(globalStart.toString())
                .endDate(end.toString())
                .habits(timelines)
                .build();
    }


}

