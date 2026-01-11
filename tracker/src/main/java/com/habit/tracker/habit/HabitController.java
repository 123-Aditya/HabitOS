package com.habit.tracker.habit;

import com.habit.tracker.habit.dto.CreateHabitRequest;
import com.habit.tracker.habit.dto.HabitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;
    
    private static final Logger LOG = LoggerFactory.getLogger(HabitController.class);

    @PostMapping
    public HabitResponse createHabit(
            @Valid @RequestBody CreateHabitRequest request) {
    	
    	LOG.info("Creating habit...");
    	String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return habitService.createHabit(request, email);
    }

    @GetMapping
    public List<HabitResponse> getMyHabits() {

    	LOG.info("Fetching habits...");
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return habitService.getMyHabits(email);
    }
}
