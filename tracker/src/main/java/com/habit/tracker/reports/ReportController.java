package com.habit.tracker.reports;

import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.habit.tracker.reports.dto.DateRangeReportResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final DateRangeReportService dateRangeReportService;
    
    private final CsvExportService csvExportService;

    @GetMapping(value = "/date-range/csv", produces = "text/csv")
    public void exportDateRangeReportCsv(
            @RequestParam String from,
            @RequestParam String to,
            HttpServletResponse response
    ) throws IOException {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        DateRangeReportResponse report =
                dateRangeReportService.generateSummary(email, from, to);

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=habit-report-" + from + "_to_" + to + ".csv"
        );

        csvExportService.writeDateRangeReport(response.getWriter(), report);
    }

}

