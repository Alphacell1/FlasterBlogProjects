import { Component } from '@angular/core';
import { BlogService } from '../../services/blog.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-blog-form',
  templateUrl: './blog-form.component.html',
  styleUrls: ['./blog-form.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class BlogFormComponent {
  title = '';
  content = '';
  errorMsg = '';

  constructor(private blogService: BlogService, private router: Router) {}

  onSubmit(): void {
    // Create the new blog post
    this.blogService.createBlog({ title: this.title, content: this.content })
      .subscribe({
        next: (response) => {
          // On success, navigate back to the blog list
          this.router.navigate(['/blogs']);
        },
        error: (err) => {
          this.errorMsg = err.error || 'Error creating blog';
          console.error(err);
        }
      });
  }
}
