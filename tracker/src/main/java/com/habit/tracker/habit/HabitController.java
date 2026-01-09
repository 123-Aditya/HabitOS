package com.habit.tracker.habit;

import com.habit.tracker.habit.dto.CreateHabitRequest;
import com.habit.tracker.habit.dto.HabitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public HabitResponse createHabit(
            @Valid @RequestBody CreateHabitRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return habitService.createHabit(request, email);
    }

    @GetMapping
    public List<HabitResponse> getMyHabits() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return habitService.getMyHabits(email);
    }
}
