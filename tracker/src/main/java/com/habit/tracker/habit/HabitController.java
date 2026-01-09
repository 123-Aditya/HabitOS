package com.habit.tracker.habit;

import com.habit.tracker.habit.dto.CreateHabitRequest;
import com.habit.tracker.habit.dto.HabitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    // TEMP: Replace with JWT extraction later
    private final String DEMO_USER_EMAIL = "aditya@test.com";

    @PostMapping
    public HabitResponse createHabit(
            @Valid @RequestBody CreateHabitRequest request) {
        return habitService.createHabit(request, DEMO_USER_EMAIL);
    }

    @GetMapping
    public List<HabitResponse> getMyHabits() {
        return habitService.getMyHabits(DEMO_USER_EMAIL);
    }
}

