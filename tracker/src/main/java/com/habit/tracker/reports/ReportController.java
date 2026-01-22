package com.habit.tracker.reports;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.habit.tracker.reports.dto.DateRangeReportResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final DateRangeReportService reportService;

    @GetMapping("/summary")
    public DateRangeReportResponse getSummary(
            @RequestParam String from,
            @RequestParam String to) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return reportService.generateSummary(email, from, to);
    }
}

