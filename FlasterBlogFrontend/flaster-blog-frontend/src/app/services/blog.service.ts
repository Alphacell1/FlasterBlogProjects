import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private apiUrl = '/api/blogs';

  constructor(private http: HttpClient, private authService: AuthService) {}

  getAllBlogs(): Observable<any[]> {
    const token = this.authService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return this.http.get<any[]>(this.apiUrl, { headers });
  }

  // NEW method:
  createBlog(blogData: { title: string, content: string }): Observable<any> {
    const token = this.authService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return this.http.post<any>(this.apiUrl, blogData, { headers });
  }
}
