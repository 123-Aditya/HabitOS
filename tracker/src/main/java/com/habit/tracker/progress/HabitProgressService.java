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

}

