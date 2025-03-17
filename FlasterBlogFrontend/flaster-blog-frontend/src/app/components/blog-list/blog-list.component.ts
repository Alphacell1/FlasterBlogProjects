import { Component } from '@angular/core';
import { NgFor } from '@angular/common';
import { BlogService } from '../../services/blog.service';

@Component({
  selector: 'app-blog-list',
  template: `
    <h2>Blogs</h2>
    <div *ngFor="let blog of blogs">
      <h3>{{ blog.title }}</h3>
      <p>{{ blog.content }}</p>
    </div>
  `,
  standalone: true,
  imports: [NgFor]
})
export class BlogListComponent {
  blogs: any[] = [];

  constructor(private blogService: BlogService) {}

  ngOnInit(): void {
    this.blogService.getAllBlogs().subscribe({
      next: (data) => this.blogs = data,
      error: (err) => console.error('Error fetching blogs:', err)
    });
  }
}
