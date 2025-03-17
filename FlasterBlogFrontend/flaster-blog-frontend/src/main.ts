import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptorsFromDi  } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { JwtInterceptor } from './app/services/jwt.interceptor';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    { provide: JwtInterceptor, useClass: JwtInterceptor },
    provideHttpClient(withInterceptorsFromDi()),
  ]
}).catch(err => console.error(err));
