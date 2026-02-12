import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-habits',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './habits.component.html',
  styleUrls: ['./habits.component.css']
})
export class HabitsComponent implements OnInit {

  habits: any[] = [];
  loading = true;

  newHabit = {
    name: '',
    frequency: 'DAILY',
    targetCount: 1
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadHabits();
  }

  loadHabits() {
    this.http.get<any[]>('http://localhost:8080/api/habits')
      .subscribe({
        next: (res) => {
          console.log("Habits:", res);
          this.habits = res;
          this.loading = false;
        },
        error: (err) => {
          console.error("Error loading habits:", err);
          this.loading = false;
        }
      });
  }

  addHabit() {
    if (!this.newHabit.name) return;

    this.http.post('http://localhost:8080/api/habits', this.newHabit)
      .subscribe(() => {
        this.newHabit = {
          name: '',
          frequency: 'DAILY',
          targetCount: 1
        };
        this.loadHabits();
      });
  }

  deleteHabit(id: number) {
    this.http.delete(`http://localhost:8080/api/habits/${id}`)
      .subscribe(() => this.loadHabits());
  }

  completeHabit(id: number) {
    this.http.post(
      `http://localhost:8080/api/habits/${id}/complete`,
      {}
    ).subscribe(() => {
      alert("Marked completed!");
    });
  }
}