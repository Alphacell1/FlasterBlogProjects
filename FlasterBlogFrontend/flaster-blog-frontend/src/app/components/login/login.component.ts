import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  email: string = '';    // Only used during register
  errorMessage = '';

  isRegisterMode = false;
  rememberMe = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    // If you want to recall the username from localStorage:
    const savedUsername = localStorage.getItem('saved_username');
    if (savedUsername) {
      this.username = savedUsername;
      this.rememberMe = true;
    }
  }

  toggleMode(): void {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = '';
  }

  onSubmit(): void {
    if (this.isRegisterMode) {
      this.register();
    } else {
      this.login();
    }
  }

  private login(): void {
    this.authService.login(this.username, this.password).subscribe({
      next: data => {
        // Store token
        this.authService.storeToken(data.token);

        // If "Remember Me" is checked, save the username locally.
        // If not, clear any existing saved username.
        if (this.rememberMe) {
          localStorage.setItem('saved_username', this.username);
        } else {
          localStorage.removeItem('saved_username');
        }

        this.errorMessage = '';
        this.router.navigate(['/blogs']); // Or wherever you want on successful login
      },
      error: error => {
        this.errorMessage = error.message ?? 'Login failed';
        console.error('Login Error:', error);
      }
    });
  }

  role: string = 'READER';  // default if you like

  private register(): void {
    this.authService.register(this.username, this.password, this.email, this.role).subscribe({
      next: (res) => {
        // "res" is { message: "User registered successfully!" } from the backend
        Swal.fire({
          icon: 'success',
          title: 'Success',
          text: res.message,
          timer: 2000,
          showConfirmButton: false
        });
  
        // Redirect to the blog list or go straight to login:
        this.router.navigate(['/blogs']);
      },
      error: (err) => {
        // The backend might have returned 400 with { message: "...some error..." }
        const backendMessage = err.error?.message || 'Registration failed';
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: backendMessage
        });
      }
    });
  }
  

}
