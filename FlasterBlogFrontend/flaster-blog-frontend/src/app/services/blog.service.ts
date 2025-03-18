import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private apiUrl = '/api/blogs';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getAllBlogs(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getBlogById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  createBlog(data: { title: string; content: string }): Observable<any> {
    let headers = this.attachAuthHeader();
    return this.http.post<any>(this.apiUrl, data, { headers });
  }

  updateBlog(id: number, data: { title: string; content: string }): Observable<any> {
    let headers = this.attachAuthHeader();
    return this.http.put<any>(`${this.apiUrl}/${id}`, data, { headers });
  }

  deleteBlog(id: number): Observable<void> {
    let headers = this.attachAuthHeader();
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }

  private attachAuthHeader(): HttpHeaders {
    const token = this.authService.getToken();
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  addComment(blogId: number, message: string): Observable<any> {
    const headers = this.attachAuthHeader();
    return this.http.post<any>(`/api/comments/${blogId}`, { message }, { headers });
  }

  getComments(blogId: number): Observable<any[]> {
    const headers = this.attachAuthHeader();
    return this.http.get<any[]>(`/api/comments/${blogId}`, { headers });
  }


  likeBlog(blogId: number): Observable<void> {
    const headers = this.attachAuthHeader();
    return this.http.post<void>(`${this.apiUrl}/${blogId}/like`, {}, { headers });
  }
  
  dislikeBlog(blogId: number): Observable<void> {
    const headers = this.attachAuthHeader();
    return this.http.post<void>(`${this.apiUrl}/${blogId}/dislike`, {}, { headers });
  }
  

}
