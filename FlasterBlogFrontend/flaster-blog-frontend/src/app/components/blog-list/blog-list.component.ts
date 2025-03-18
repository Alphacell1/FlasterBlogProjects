import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogService } from '../../services/blog.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-blog-list',
  templateUrl: './blog-list.component.html',
  styleUrls: ['./blog-list.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})

export class BlogListComponent implements OnInit {
  blogs: any[] = [];

  constructor(
    private blogService: BlogService,
    private authService: AuthService,
    private router: Router
  ) { }

  // Typical approach in blog-list.component.ts:
  ngOnInit(): void {
    this.blogService.getAllBlogs().subscribe(blogs => {
      this.blogs = blogs.map((b: any) => {
        // Convert likedBy user objects to string usernames
        b.likedBy = (b.likedBy || []).map((u: any) =>
          typeof u === 'object' && u.username ? u.username : u
        );
        // Convert dislikedBy user objects to string usernames
        b.dislikedBy = (b.dislikedBy || []).map((u: any) =>
          typeof u === 'object' && u.username ? u.username : u
        );
    
        return {
          ...b,
          comments: [],
          newComment: ''
        };
      });
    
      // Then fetch comments, etc.
      this.blogs.forEach(blog => {
        this.blogService.getComments(blog.id).subscribe(res => {
          blog.comments = res;
        });
      });
    });    
  }


  loadBlogs(): void {
    // 1) Get all blogs from backend
    this.blogService.getAllBlogs().subscribe({
      next: (data) => {
        // We'll add an empty 'comments' array and 'newComment' field to each blog for convenience
        this.blogs = data.map(blog => ({
          ...blog,
          comments: [],
          newComment: ''
        }));

        // 2) For each blog, load its comments
        this.blogs.forEach(b => {
          this.blogService.getComments(b.id).subscribe({
            next: (comments) => {
              b.comments = comments;
            },
            error: (err) => {
              console.error('Error fetching comments for blog ' + b.id, err);
            }
          });
        });
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
        this.blogs = this.blogs.filter(b => b.id !== blogId);
        Swal.fire('Deleted!', 'Your blog post was deleted.', 'success');
      },
      error: (err) => {
        console.error('Error deleting blog:', err);
        if (err.status === 403) {
          Swal.fire('Forbidden', 'You can only delete your own posts.', 'error');
        } else {
          Swal.fire('Error', 'Failed to delete the post.', 'error');
        }
      }
    });
  }

  // Add a new comment to the given blog
  addComment(blog: any): void {
    const message = blog.newComment.trim();
    if (!message) return; // skip empty

    this.blogService.addComment(blog.id, message).subscribe({
      next: (newComment) => {
        blog.comments.push(newComment);
        blog.newComment = '';
      },
      error: (err) => {
        console.error('Error adding comment:', err);
        Swal.fire('Error', 'Could not add comment', 'error');
      }
    });
  }

  likeBlog(blog: any): void {
    this.blogService.likeBlog(blog.id).subscribe({
      next: () => {
        // Update local likedBy, remove user from dislikedBy
        const currentUser = this.authService.getUsername(); // or store "sub"
        // if you don't have getUsername, parse the local token
  
        if (!blog.likedBy) blog.likedBy = [];
        if (!blog.dislikedBy) blog.dislikedBy = [];
  
        // remove from dislikedBy if present
        blog.dislikedBy = blog.dislikedBy.filter((u: string) => u !== currentUser);
  
        // add to likedBy if not present
        if (!blog.likedBy.includes(currentUser)) {
          blog.likedBy.push(currentUser);
        }
      },
      error: err => console.error('Error liking blog:', err)
    });
  }
  
  dislikeBlog(blog: any): void {
    this.blogService.dislikeBlog(blog.id).subscribe({
      next: () => {
        const currentUser = this.authService.getUsername(); 
        if (!blog.likedBy) blog.likedBy = [];
        if (!blog.dislikedBy) blog.dislikedBy = [];
  
        // remove from likedBy
        blog.likedBy = blog.likedBy.filter((u: string) => u !== currentUser);
  
        // add to dislikedBy if not present
        if (!blog.dislikedBy.includes(currentUser)) {
          blog.dislikedBy.push(currentUser);
        }
      },
      error: err => console.error('Error disliking blog:', err)
    });
  }

  formatLikesTooltip(likedBy: string[]): string {
    if (!likedBy || likedBy.length === 0) return 'No one yet';
    const firstThree = likedBy.slice(0, 3).join(', ');
    if (likedBy.length > 3) {
      return `${firstThree}... and ${likedBy.length - 3} more`;
    }
    return firstThree;
  }
  
  formatDislikesTooltip(dislikedBy: string[]): string {
    if (!dislikedBy || dislikedBy.length === 0) return 'No one yet';
    const firstThree = dislikedBy.slice(0, 3).join(', ');
    if (dislikedBy.length > 3) {
      return `${firstThree}... and ${dislikedBy.length - 3} more`;
    }
    return firstThree;
  }
  
  
}
