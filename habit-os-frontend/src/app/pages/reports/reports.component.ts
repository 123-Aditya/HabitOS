import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent {

  constructor(private http: HttpClient) {}

  fromDate: string = '';
  toDate: string = '';

  loading = false;
  reportData: any = null;

  weeklyAnalytics: any = null;
  monthlyAnalytics: any = null;

  loadReport() {

    if (!this.fromDate || !this.toDate) return;

    this.loading = true;

    this.http.get<any>(
      `http://localhost:8080/api/habits/progress/bulk?from=${this.fromDate}&to=${this.toDate}`
    ).subscribe({
      next: res => {
        console.log("REPORT RESPONSE:", res);
        this.reportData = this.processReport(res);
        this.loading = false;
      },
      error: err => {
        console.error("Error loading report:", err);
        this.loading = false;
      }
    });
  }

  processReport(data: any) {

    const habits = data.habits.map((habit: any) => {

      const totalDays = habit.progress.length;
      const completed = habit.progress.filter(
        (p: any) => p.status === 'COMPLETED'
      ).length;

      const missed = totalDays - completed;

      const completionPercentage =
        totalDays === 0 ? 0 :
        Math.round((completed / totalDays) * 100);

      return {
        habitName: habit.habitName,
        totalDays,
        completed,
        missed,
        completionPercentage
      };
    });

    return {
      startDate: data.startDate,
      endDate: data.endDate,
      habits
    };
  }

loadWeeklyAnalytics() {

  this.loading = true;

  this.http.get<any>(
    'http://localhost:8080/api/analytics/weekly'
  ).subscribe({
    next: res => {
      console.log("WEEKLY:", res);
      this.weeklyAnalytics = res;
      this.loading = false;
    },
    error: err => {
      console.error("Weekly analytics error:", err);
      this.loading = false;
    }
  });
}

loadMonthlyAnalytics() {

  this.loading = true;

  this.http.get<any>(
    'http://localhost:8080/api/analytics/monthly'
  ).subscribe({
    next: res => {
      console.log("MONTHLY:", res);
      this.monthlyAnalytics = res;
      this.loading = false;
    },
    error: err => {
      console.error("Monthly analytics error:", err);
      this.loading = false;
    }
  });
}

downloadCSV() {

  if (!this.fromDate || !this.toDate) return;

  const url =
    `http://localhost:8080/api/reports/date-range/csv?from=${this.fromDate}&to=${this.toDate}`;

  window.open(url, '_blank');
}
}