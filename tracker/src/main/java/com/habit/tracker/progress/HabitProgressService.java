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

        return getProgress(habitId, userEmail, null, null);
    }

    public HabitProgressResponse getProgress(
            Long habitId,
            String userEmail,
            String from,
            String to) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized access");
        }

        LocalDate habitStart = habit.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        LocalDate start = parseDate(from, habitStart);
        LocalDate end = parseDate(to, today);

        // validation
        if (start.isAfter(end)) {
            throw new BadRequestException("'from' date cannot be after 'to' date");
        }

        // clamp range
        start = start.isBefore(habitStart) ? habitStart : start;
        end = end.isAfter(today) ? today : end;

        List<DailyProgressResponse> progressList = new ArrayList<>();

        for (LocalDate date = start;
             !date.isAfter(end);
             date = date.plusDays(1)) {

            Optional<HabitEntry> entry =
                    entryRepository.findByHabitAndEntryDate(habit, date);

            ProgressStatus status;

            if (entry.isPresent()) {
                status = entry.get().getStatus() == HabitEntryStatus.DONE
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
    
    
    public BulkHabitProgressResponse getBulkProgress(
            String userEmail,
            String from,
            String to) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Habit> habits = habitRepository.findByUserAndActiveTrue(user);

        if (habits.isEmpty()) {
            return BulkHabitProgressResponse.builder()
                    .habits(List.of())
                    .build();
        }

        LocalDate today = LocalDate.now();

        LocalDate globalStart = habits.stream()
                .map(h -> h.getCreatedAt().toLocalDate())
                .min(LocalDate::compareTo)
                .get();

        LocalDate start = parseDate(from, globalStart);
        LocalDate end = parseDate(to, today);

        if (start.isAfter(end)) {
            throw new BadRequestException("'from' date cannot be after 'to' date");
        }

        start = start.isBefore(globalStart) ? globalStart : start;
        end = end.isAfter(today) ? today : end;

        List<HabitTimelineResponse> timelines = new ArrayList<>();

        for (Habit habit : habits) {

            HabitProgressResponse single =
                    getProgress(habit.getId(), userEmail,
                            start.toString(), end.toString());

            timelines.add(
                    HabitTimelineResponse.builder()
                            .habitId(single.getHabitId())
                            .habitName(single.getHabitName())
                            .progress(single.getProgress())
                            .build()
            );
        }

        return BulkHabitProgressResponse.builder()
                .startDate(start.toString())
                .endDate(end.toString())
                .habits(timelines)
                .build();
    }

    
    private LocalDate parseDate(String value, LocalDate defaultValue) {
        return value == null ? defaultValue : LocalDate.parse(value);
    }

}

