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
      this.data = res;

      if (res?.habits) {
        this.totalHabits = res.habits.length;
        this.totalEntries = res.habits.reduce(
          (sum: number, h: any) => sum + (h.progress?.length || 0),
          0
        );

        if (this.totalHabits > 0) {
          this.completionPercentage =
            Math.round((this.totalEntries / this.totalHabits) * 100);
        } else {
          this.completionPercentage = 0;
        }
      }

      this.loading = false;
    },
    error: (err) => {
      console.error(err);
      this.loading = false;
    }
  });
}

}