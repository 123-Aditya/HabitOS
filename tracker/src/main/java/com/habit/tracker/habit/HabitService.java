package com.habit.tracker.habit;

import com.habit.tracker.exception.ResourceNotFoundException;
import com.habit.tracker.habit.dto.CreateHabitRequest;
import com.habit.tracker.habit.dto.HabitResponse;
import com.habit.tracker.user.User;
import com.habit.tracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitResponse createHabit(CreateHabitRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Habit habit = Habit.builder()
                .name(request.getName())
                .description(request.getDescription())
                .frequency(request.getFrequency())
                .targetCount(request.getTargetCount())
                .active(true)
                .user(user)
                .build();

        Habit saved = habitRepository.save(habit);

        return mapToResponse(saved);
    }

    public List<HabitResponse> getMyHabits(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return habitRepository.findByUserAndActiveTrue(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private HabitResponse mapToResponse(Habit habit) {
        return HabitResponse.builder()
                .id(habit.getId())
                .name(habit.getName())
                .description(habit.getDescription())
                .frequency(habit.getFrequency())
                .targetCount(habit.getTargetCount())
                .active(habit.getActive())
                .build();
    }
}

