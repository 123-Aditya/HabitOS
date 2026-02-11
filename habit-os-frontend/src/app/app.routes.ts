import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HabitsComponent } from './pages/habits/habits.component';
import { ReportsComponent } from './pages/reports/reports.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { LoginComponent } from './auth/login/login.component';

export const routes: Routes = [

  { path: 'login', component: LoginComponent, title: 'Login | Habit OS' },

  {
    path: '', component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent, title: 'Dashboard | Habit OS' },
      { path: 'habits', component: HabitsComponent, title: 'Habits | Habit OS' },
      { path: 'reports', component: ReportsComponent, title: 'Reports | Habit OS' },
      { path: 'settings', component: SettingsComponent, title: 'Settings | Habit OS' }
    ]
  },
  
  { path: '**', redirectTo: 'login' }
];
