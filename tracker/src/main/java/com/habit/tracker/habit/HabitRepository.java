package com.habit.tracker.habit;

import com.habit.tracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUserAndActiveTrue(User user);
    
    Optional<Habit> findByUserAndNameIgnoreCase(User user, String name);

}

