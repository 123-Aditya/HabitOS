import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/core/interceptors/auth.interceptor';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';   // ✅ IMPORT YOUR REAL ROUTES

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),   // ✅ USE app.routes.ts
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
}).catch(err => console.error(err));