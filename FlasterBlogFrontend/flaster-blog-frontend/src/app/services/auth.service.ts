import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';



interface DecodedToken {
  sub: string;     // username
  roles: string[]; // e.g. ["ROLE_AUTHOR", "ROLE_READER"]
  exp: number;     // expiration
  iat: number;     // issued at
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = '/api/auth';  // proxy-config handles this

  constructor(private http: HttpClient) {}

  // Login: get { token }, decode JWT to store roles in localStorage
  login(username: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.authUrl}/login`, { username, password })
      .pipe(
        tap(res => {
          if (res.token) {
            this.storeToken(res.token);

            // Decode roles from the token
            const decoded = jwtDecode<DecodedToken>(res.token);
            const roles = decoded.roles || [];
            localStorage.setItem('user_roles', JSON.stringify(roles));
          }
        }),
        catchError(this.handleError)
      );
  }

  // Register new user (backend returns { message: string })
  register(username: string, password: string, email: string, role: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(
      `${this.authUrl}/register`,
      { username, password, email, role }
    ).pipe(
      catchError(this.handleError)
    );
  }

  // === Token / Role helpers ===
  storeToken(token: string): void {
    localStorage.setItem('jwt', token);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  logout(): void {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user_roles');
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  // Check if current user has ROLE_AUTHOR
  isAuthor(): boolean {
    const rolesJson = localStorage.getItem('user_roles');
    if (!rolesJson) return false;
    const roles: string[] = JSON.parse(rolesJson);
    return roles.includes('ROLE_AUTHOR');
  }

  // === Error handling ===
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // client-side or network error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // backend error
      errorMessage = `Server Error (${error.status}): ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
