import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface LoginResponse {
  token: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = '';
  password = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(): void {
    const credentials = {
      email: this.email,
      password: this.password
    };

    // 🔥 THIS is where your code goes
    this.http.post<LoginResponse>(
      'http://localhost:8080/api/auth/login',
      credentials
    ).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token); // ✅ store JWT
        this.router.navigate(['/dashboard']);     // ✅ redirect
      },
      error: () => {
        alert('Invalid email or password');
      }
    });
  }
}
