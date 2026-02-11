import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
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

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];

    this.http.get<any>(
      `http://localhost:8080/api/habits/progress/bulk?from=${today}&to=${today}`
    ).subscribe(res => {

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
          }
      }

      this.loading = false;
    });
  }
}