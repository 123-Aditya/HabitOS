package com.habit.tracker.scheduler;

import com.habit.tracker.analytics.AnalyticsService;
import com.habit.tracker.analytics.dto.*;
import com.habit.tracker.email.EmailService;
import com.habit.tracker.user.User;
import com.habit.tracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeeklyReportScheduler {

    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;
    private final EmailService emailService;

    // Every Monday at 9 AM
    @Scheduled(cron = "0 0 9 ? * MON")
    public void sendWeeklyReports() {

        log.info("📧 Weekly report scheduler started");

        List<User> users = userRepository.findAll();

        for (User user : users) {
            try {
                PeriodAnalyticsResponse analytics =
                        analyticsService.getAnalytics(
                                user.getEmail(),
                                AnalyticsPeriod.WEEKLY
                        );

                String emailBody = buildEmailBody(user, analytics);

                emailService.sendEmail(
                        user.getEmail(),
                        "📊 Your Weekly Habit Report",
                        emailBody
                );

            } catch (Exception e) {
                log.error(
                        "Failed to send weekly report to {}",
                        user.getEmail(),
                        e
                );
            }
        }

        log.info("✅ Weekly report scheduler completed");
    }

    private String buildEmailBody(
            User user,
            PeriodAnalyticsResponse analytics) {

        StringBuilder sb = new StringBuilder();

        sb.append("Hi ").append(user.getName()).append(",\n\n");
        sb.append("Here is your weekly habit summary:\n\n");

        for (HabitAnalyticsResponse habit : analytics.getHabits()) {
            sb.append("• ").append(habit.getHabitName()).append("\n");
            sb.append("  Done: ").append(habit.getDoneCount()).append("\n");
            sb.append("  Missed: ").append(habit.getMissedCount()).append("\n");
            sb.append("  Skipped: ").append(habit.getSkippedCount()).append("\n");
            sb.append("  Consistency: ")
              .append(habit.getConsistencyPercentage()).append("%\n");
            sb.append("  Current Streak: ")
              .append(habit.getCurrentStreak()).append("\n\n");
        }

        sb.append("Keep going 💪\n");
        sb.append("Small steps, big consistency.\n\n");
        sb.append("— Habit Tracker");

        return sb.toString();
    }
    
    @GetMapping("/test-weekly-email")
    public void testWeeklyEmail() {
        sendWeeklyReports();
    }

}

