import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private http: HttpClient) {}

  data: any;
  loading = true;

  totalHabits = 0;
  totalEntries = 0;
  completionPercentage = 0;

  fromDate!: string;
  toDate!: string;

  summary: any = null;

  ngOnInit(): void {
      const today = new Date().toISOString().split('T')[0];

      this.fromDate = today;
      this.toDate = today;

      this.loadDashboard();
  }

loadDashboard(): void {

  if (!this.fromDate || !this.toDate) 
    return;

  this.loading = true;

  this.http.get<any>(
    `http://localhost:8080/api/habits/progress/bulk?from=${this.fromDate}&to=${this.toDate}`
    ).subscribe({
      next: (res) => {

    console.log("API RESPONSE:", res);

    const habits = res.habits || [];

    const today = new Date().toISOString().split('T')[0];

    let totalExpected = 0;
    let totalCompleted = 0;
    let todayCompleted = 0;

    habits.forEach((habit: any) => {

      totalExpected += habit.progress.length;

      habit.progress.forEach((entry: any) => {

        if (entry.status === 'COMPLETED') {
          totalCompleted++;

          if (entry.date === today) {
            todayCompleted++;
          }
        }

      });
    });

    const completionPercentage =
      totalExpected === 0
        ? 0
        : Math.round((totalCompleted / totalExpected) * 100);

    this.summary = {
      totalHabits: habits.length,
      totalDays: habits[0]?.progress?.length || 0,
      overallCompletionPercentage: completionPercentage,
      todayCompleted: todayCompleted
    };

    console.log("Calculated Summary:", this.summary);

    this.loading = false;
  },
  error: (err) => {
    console.error("Dashboard API Error:", err);
    this.loading = false;
  }
});
}

}