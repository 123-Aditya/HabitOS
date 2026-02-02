import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTabsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  loading = true;

  // Temporary profile model (backend later)
  profile = {
    name: '',
    email: ''
  };

  ngOnInit(): void {
    // Fake load for now (we’ll connect backend next)
    setTimeout(() => {
      this.profile = {
        name: 'Aditya',
        email: 'aditya@email.com'
      };
      this.loading = false;
    }, 500);
  }

  saveProfile(): void {
    console.log('Profile saved:', this.profile);
    alert('Profile saved');
  }
}
