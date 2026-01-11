package com.habit.tracker.entry;

import com.habit.tracker.entry.dto.LogHabitRequest;
import com.habit.tracker.entry.dto.HabitEntryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class HabitEntryController {

    private final HabitEntryService entryService;
    
    private static final Logger LOG = LoggerFactory.getLogger(HabitEntryController.class);

    @PostMapping
    public HabitEntryResponse logHabit(
            @Valid @RequestBody LogHabitRequest request) {
    	
    	LOG.info("Logging habit...");

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return entryService.logHabit(request, email);
    }
}

