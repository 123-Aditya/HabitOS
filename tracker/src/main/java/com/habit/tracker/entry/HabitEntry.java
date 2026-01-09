package com.habit.tracker.entry;

import com.habit.tracker.common.BaseEntity;
import com.habit.tracker.habit.Habit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "habit_entries",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"habit_id", "entry_date"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HabitEntryStatus status;

    private Integer value; // optional (hours, count)

    private String notes;
}
