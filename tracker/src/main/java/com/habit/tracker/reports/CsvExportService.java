package com.habit.tracker.reports;

import com.habit.tracker.reports.dto.DateRangeReportResponse;
import com.habit.tracker.reports.dto.HabitSummaryResponse;
import org.springframework.stereotype.Service;
import java.io.PrintWriter;

@Service
public class CsvExportService {

    public void writeDateRangeReport(
            PrintWriter writer,
            DateRangeReportResponse report
    ) {

        // Header metadata
        writer.println("From," + report.getFrom());
        writer.println("To," + report.getTo());
        writer.println("Total Days," + report.getTotalDays());
        writer.println("Total Habits," + report.getTotalHabits());
        writer.println("Overall Completion (%)," +
                report.getOverallCompletionPercentage());
        writer.println();

        // Table header
        writer.println(
                "Habit Name,Frequency,Target,Expected Count,Completed Count,Completion %,Current Streak"
        );

        for (HabitSummaryResponse habit : report.getHabitSummaries()) {
            writer.println(
                    csv(habit.getHabitName()) + "," +
                    habit.getFrequency() + "," +
                    habit.getTarget() + "," +
                    habit.getExpectedCount() + "," +
                    habit.getCompletedCount() + "," +
                    habit.getCompletionPercentage() + "," +
                    habit.getCurrentStreak()
            );
        }

        writer.flush();
    }

    private String csv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}

