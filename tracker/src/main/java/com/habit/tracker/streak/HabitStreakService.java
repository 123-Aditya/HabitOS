package com.habit.tracker.streak;

import com.habit.tracker.entry.HabitEntry;
import com.habit.tracker.entry.HabitEntryRepository;
import com.habit.tracker.entry.HabitEntryStatus;
import com.habit.tracker.habit.Habit;
import com.habit.tracker.skip.HabitSkipRuleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitStreakService {

    private final HabitEntryRepository entryRepository;
    
    private final HabitSkipRuleRepository skipRuleRepository;

    @Cacheable(
    	    value = "habit-streak",
    	    key = "#habit.id"
    	)
    public HabitStreak calculateStreak(Habit habit) {

        List<HabitEntry> entries =
                entryRepository.findByHabitOrderByEntryDateDesc(habit);

        int currentStreak = 0;
        int longestStreak = 0;
        int tempStreak = 0;

        LocalDate expectedDate = LocalDate.now();

        for (HabitEntry entry : entries) {

            if (entry.getEntryDate().isAfter(expectedDate)) {
                continue;
            }

            if (!entry.getEntryDate().equals(expectedDate)) {

                boolean isSkipped = skipRuleRepository
                        .existsByHabitAndDayOfWeek(
                            habit,
                            expectedDate.getDayOfWeek()
                        )
                    || skipRuleRepository
                        .existsByHabitAndSpecificDate(
                            habit,
                            expectedDate
                        );

                if (isSkipped) {
                    expectedDate = expectedDate.minusDays(1);
                    continue;
                }

                break;
            }


            if (entry.getStatus() == HabitEntryStatus.DONE) {
                tempStreak++;
                currentStreak++;
                longestStreak = Math.max(longestStreak, tempStreak);
            }

            if (entry.getStatus() == HabitEntryStatus.SKIPPED) {
                // skip does not increment or break
            }

            expectedDate = expectedDate.minusDays(1);
        }

        return new HabitStreak(currentStreak, longestStreak);
    }
}

