package com.habit.tracker.entry;

import com.habit.tracker.habit.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface HabitEntryRepository
        extends JpaRepository<HabitEntry, Long> {

    Optional<HabitEntry> findByHabitAndEntryDate(
            Habit habit,
            LocalDate entryDate
    );

    List<HabitEntry> findByHabitOrderByEntryDateDesc(
            Habit habit
    );
}

