package com.habit.tracker.skip;

import com.habit.tracker.habit.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface HabitSkipRuleRepository
        extends JpaRepository<HabitSkipRule, Long> {

    List<HabitSkipRule> findByHabit(Habit habit);

    boolean existsByHabitAndDayOfWeek(Habit habit, DayOfWeek dayOfWeek);

    boolean existsByHabitAndSpecificDate(Habit habit, LocalDate date);
}
