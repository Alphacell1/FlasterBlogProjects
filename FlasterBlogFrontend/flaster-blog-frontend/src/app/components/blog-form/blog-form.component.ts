import { Component, OnInit } from '@angular/core';
import { BlogService } from '../../services/blog.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-blog-form',
  templateUrl: './blog-form.component.html',
  styleUrls: ['./blog-form.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class BlogFormComponent implements OnInit {
  title = '';
  content = '';
  errorMsg = '';
  editMode = false; // determine if we're creating or editing
  blogId!: number;

  constructor(
    private blogService: BlogService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Check if there's an :id param, if so, we load the existing blog
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.editMode = true;
        this.blogId = +idParam; // convert to number
        // fetch the existing blog for editing
        this.blogService.getBlogById(this.blogId).subscribe({
          next: blog => {
            this.title = blog.title;
            this.content = blog.content;
          },
          error: err => {
            this.errorMsg = err.error || 'Error fetching blog';
            console.error(err);
          }
        });
      }
    });
  }

  onSubmit(): void {
    if (this.editMode) {
      // Update existing post
      this.blogService.updateBlog(this.blogId, { title: this.title, content: this.content })
        .subscribe({
          next: () => {
            this.router.navigate(['/blogs']);
          },
          error: (err) => {
            this.errorMsg = err.error || 'Error updating blog';
            console.error(err);
          }
        });
    } else {
      // Create new blog
      this.blogService.createBlog({ title: this.title, content: this.content })
        .subscribe({
          next: () => {
            this.router.navigate(['/blogs']);
          },
          error: (err) => {
            this.errorMsg = err.error || 'Error creating blog';
            console.error(err);
          }
        });
    }
  }
}
