package com.habit.tracker.skip;

import com.habit.tracker.common.BaseEntity;
import com.habit.tracker.habit.Habit;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Table(name = "habit_skip_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitSkipRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // SUNDAY, SATURDAY etc.

    private LocalDate specificDate; // holiday
}
