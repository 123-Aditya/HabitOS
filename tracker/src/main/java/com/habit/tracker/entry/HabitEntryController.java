package com.habit.tracker.entry;

import com.habit.tracker.entry.dto.LogHabitRequest;
import com.habit.tracker.entry.dto.HabitEntryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class HabitEntryController {

    private final HabitEntryService entryService;

    @PostMapping
    public HabitEntryResponse logHabit(
            @Valid @RequestBody LogHabitRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return entryService.logHabit(request, email);
    }
}

