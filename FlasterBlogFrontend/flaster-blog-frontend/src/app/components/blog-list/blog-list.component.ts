import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogService } from '../../services/blog.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-blog-list',
  templateUrl: './blog-list.component.html',
  styleUrls: ['./blog-list.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class BlogListComponent implements OnInit {
  blogs: any[] = [];

  constructor(
    private blogService: BlogService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadBlogs();
  }

  loadBlogs(): void {
    this.blogService.getAllBlogs().subscribe({
      next: (data) => {
        this.blogs = data;
      },
      error: (err) => {
        console.error('Error fetching blogs:', err);
        Swal.fire('Error', 'Could not load blog posts.', 'error');
      }
    });
  }

  isAuthor(): boolean {
    return this.authService.isAuthor();
  }

  goToCreateBlog(): void {
    this.router.navigate(['/create-blog']);
  }

  editBlog(blogId: number): void {
    this.router.navigate(['/edit-blog', blogId]);
  }

  deleteBlog(blogId: number): void {
    if (!confirm('Are you sure you want to delete this blog post?')) return;
    this.blogService.deleteBlog(blogId).subscribe({
      next: () => {
        // remove from local array
        this.blogs = this.blogs.filter(b => b.id !== blogId);
        Swal.fire('Deleted!', 'Your blog post was deleted.', 'success');
      },
      error: (err) => {
        console.error('Error deleting blog:', err);
        // if 403, user isn't the author, show a message
        if (err.status === 403) {
          Swal.fire('Forbidden', 'You can only delete your own posts.', 'error');
        } else {
          Swal.fire('Error', 'Failed to delete the post.', 'error');
        }
      }
    });
  }
}
