package hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller;


import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.BlogPost;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.BlogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogRepository BlogPostRepository;

    public BlogController(BlogRepository blogRepository) {
        this.BlogPostRepository = blogRepository;
    }

    // GET all BlogPosts
    @GetMapping
    public List<BlogPost> getAllBlogPosts() {
        return BlogPostRepository.findAll();
    }

    // GET one BlogPost by ID
    @GetMapping("/{id}")
    public BlogPost getBlogPostById(@PathVariable Long id) {
        return BlogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));
    }

    // CREATE a new BlogPost
    @PostMapping
    public BlogPost createBlogPost(@RequestBody BlogPost BlogPost) {
        return BlogPostRepository.save(BlogPost);
    }

    // UPDATE a BlogPost
    @PutMapping("/{id}")
    public BlogPost updateBlogPost(@PathVariable Long id, @RequestBody BlogPost BlogPostDetails) {
        BlogPost existingBlogPost = BlogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));

        existingBlogPost.setTitle(BlogPostDetails.getTitle());
        existingBlogPost.setContent(BlogPostDetails.getContent());
        return BlogPostRepository.save(existingBlogPost);
    }

    // DELETE a BlogPost
    @DeleteMapping("/{id}")
    public void deleteBlogPost(@PathVariable Long id) {
        BlogPostRepository.deleteById(id);
    }
}
