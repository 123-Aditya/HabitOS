package com.habit.tracker.progress;

import com.habit.tracker.progress.dto.BulkHabitProgressResponse;
import com.habit.tracker.progress.dto.HabitProgressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
@Slf4j
public class HabitProgressController {

    private final HabitProgressService progressService;

    @GetMapping("/{habitId}/progress")
    public HabitProgressResponse getHabitProgress(
            @PathVariable Long habitId) {
    	
    	log.info("Fetching progress for habit {}", habitId);

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return progressService.getProgress(habitId, email);
    }
    
    
    @GetMapping("/progress")
    public HabitProgressResponse getHabitProgressByName(
            @RequestParam String name) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return progressService.getProgressByName(name, email);
    }
    
    // This API is read-heavy and compute-heavy.
    // Future Optimizations (When Needed)
    // Batch fetch entries, Batch fetch skip rules, Cache bulk response
    @GetMapping("/progress/bulk")
    public BulkHabitProgressResponse getAllHabitsProgress() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return progressService.getBulkProgress(email);
    }

}

