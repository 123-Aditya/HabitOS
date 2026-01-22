package com.habit.tracker.analytics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;
import com.habit.tracker.habit.Habit;
import com.habit.tracker.habit.HabitFrequency;
import com.habit.tracker.user.User;

@Component
public class ExpectedCountCalculator {

    public int calculateExpected(
            Habit habit,
            LocalDate from,
            LocalDate to,
            User user) {

        long days = ChronoUnit.DAYS.between(from, to) + 1;

        if (habit.getFrequency() == HabitFrequency.DAILY) {
            return (int) days;
        }

        if (habit.getFrequency() == HabitFrequency.WEEKLY) {
            long weeks = (long) Math.ceil(days / 7.0);
            return (int) (weeks * habit.getTargetCount());
        }

        return 0;
    }
}

