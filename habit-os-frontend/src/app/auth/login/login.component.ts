import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  credentials = {
    email: '',
    password: ''
  };

  loading = false;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login() {
    this.loading = true;

    this.http.post<any>(
      'http://localhost:8080/api/auth/login',
      this.credentials
    ).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        alert('Invalid credentials');
        this.loading = false;
      }
    });
  }
}
