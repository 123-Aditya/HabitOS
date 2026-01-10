package com.habit.tracker.skip;

import com.habit.tracker.skip.dto.CreateSkipRuleRequest;
import com.habit.tracker.skip.dto.SkipRuleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skip-rules")
@RequiredArgsConstructor
public class HabitSkipRuleController {

    private final HabitSkipRuleService skipRuleService;

    @PostMapping
    public SkipRuleResponse createSkipRule(
            @Valid @RequestBody CreateSkipRuleRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return skipRuleService.createSkipRule(request, email);
    }
}

