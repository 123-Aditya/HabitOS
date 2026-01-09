package com.habit.tracker.entry;

import com.habit.tracker.entry.dto.LogHabitRequest;
import com.habit.tracker.entry.dto.HabitEntryResponse;
import com.habit.tracker.habit.Habit;
import com.habit.tracker.habit.HabitRepository;
import com.habit.tracker.user.User;
import com.habit.tracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HabitEntryService {

    private final HabitEntryRepository entryRepository;
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitEntryResponse logHabit(
            LogHabitRequest request,
            String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Habit habit = habitRepository.findById(request.getHabitId())
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized habit access");
        }

        entryRepository.findByHabitAndEntryDate(
                habit, request.getDate()
        ).ifPresent(e -> {
            throw new RuntimeException(
                    "Habit already logged for this date");
        });

        HabitEntry entry = HabitEntry.builder()
                .habit(habit)
                .entryDate(request.getDate())
                .status(request.getStatus())
                .value(request.getValue())
                .notes(request.getNotes())
                .build();

        HabitEntry saved = entryRepository.save(entry);

        return mapToResponse(saved);
    }

    private HabitEntryResponse mapToResponse(HabitEntry entry) {
        return HabitEntryResponse.builder()
                .id(entry.getId())
                .habitId(entry.getHabit().getId())
                .date(entry.getEntryDate())
                .status(entry.getStatus())
                .value(entry.getValue())
                .notes(entry.getNotes())
                .build();
    }
}
