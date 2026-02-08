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

  email = '';
  password = '';

  loading = false;
  errorMessage = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  get isEmailValid(): boolean {
    return (
      this.email.endsWith('@gmail.com') ||
      this.email.endsWith('@yahoo.com')
    );
  }

  get isPasswordValid(): boolean {
    return this.password.length >= 8;
  }

  get isFormValid(): boolean {
    return this.isEmailValid && this.isPasswordValid;
  }

  login(): void {

    if (!this.isFormValid) 
      return;

    this.errorMessage = '';
    this.loading = true;

    this.http.post<any>('http://localhost:8080/api/auth/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: res => {
        localStorage.setItem('token', res.token);
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: err => {
        this.loading = false;

        // Error messages
        if (err.status === 401) {
          this.errorMessage = 'Invalid email or password';
        } else if (err.status === 0) {
          this.errorMessage = 'Login failed.Please try again after some time.';
        } else {
          this.errorMessage = 'Something went wrong. Try again.';
        }
      }
    });
  }
}