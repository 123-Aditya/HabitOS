package com.habit.tracker.skip;

import com.habit.tracker.exception.BadRequestException;
import com.habit.tracker.exception.ResourceNotFoundException;
import com.habit.tracker.exception.UnauthorizedException;
import com.habit.tracker.habit.Habit;
import com.habit.tracker.habit.HabitRepository;
import com.habit.tracker.skip.dto.CreateSkipRuleRequest;
import com.habit.tracker.skip.dto.SkipRuleResponse;
import com.habit.tracker.user.User;
import com.habit.tracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitSkipRuleService {

    private final HabitRepository habitRepository;
    private final HabitSkipRuleRepository skipRuleRepository;
    private final UserRepository userRepository;

    @CacheEvict(
    	    value = "habit-streak",
    	    key = "#request.habitId"
    	)
    public SkipRuleResponse createSkipRule(
            CreateSkipRuleRequest request,
            String userEmail) {

        if (request.getDayOfWeek() == null &&
            request.getSpecificDate() == null) {
            throw new BadRequestException("Invalid skip rule");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Habit habit = habitRepository.findById(request.getHabitId())
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized");
        }

        if (request.getDayOfWeek() != null &&
            skipRuleRepository.existsByHabitAndDayOfWeek(
                    habit, request.getDayOfWeek())) {
            throw new BadRequestException("Skip rule already exists");
        }

        if (request.getSpecificDate() != null &&
            skipRuleRepository.existsByHabitAndSpecificDate(
                    habit, request.getSpecificDate())) {
            throw new BadRequestException("Skip rule already exists");
        }

        HabitSkipRule rule = HabitSkipRule.builder()
                .habit(habit)
                .dayOfWeek(request.getDayOfWeek())
                .specificDate(request.getSpecificDate())
                .build();

        HabitSkipRule saved = skipRuleRepository.save(rule);

        return SkipRuleResponse.builder()
                .id(saved.getId())
                .dayOfWeek(saved.getDayOfWeek())
                .specificDate(saved.getSpecificDate())
                .build();
    }
}

