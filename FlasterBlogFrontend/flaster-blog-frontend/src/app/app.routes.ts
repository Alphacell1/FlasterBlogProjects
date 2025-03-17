import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { BlogListComponent } from './components/blog-list/blog-list.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  { path: 'login', component: LoginComponent },
  { path: 'blogs', component: BlogListComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: 'login' }
];
