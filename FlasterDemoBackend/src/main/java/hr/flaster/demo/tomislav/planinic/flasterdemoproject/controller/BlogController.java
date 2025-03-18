package hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller;


import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.BlogPost;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.User;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.BlogRepository;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    public BlogController(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    // GET all BlogPosts
    @GetMapping
    public List<BlogPost> getAllBlogPosts() {
        return blogRepository.findAll();
    }

    // GET one BlogPost by ID
    @GetMapping("/{id}")
    public BlogPost getBlogPostById(@PathVariable Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));
    }

    // CREATE a new BlogPost
    @PostMapping
    public BlogPost createBlogPost(@RequestBody BlogPost blogPost, Principal principal) {
        System.out.println("Creating new blog by user: " + principal.getName());
        User author = userRepository.findByUsername(principal.getName());
        blogPost.setAuthor(author);
        return blogRepository.save(blogPost);
    }

    // UPDATE a BlogPost
    @PutMapping("/{id}")
    public BlogPost updateBlogPost(@PathVariable Long id, @RequestBody BlogPost BlogPostDetails) {
        BlogPost existingBlogPost = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));

        existingBlogPost.setTitle(BlogPostDetails.getTitle());
        existingBlogPost.setContent(BlogPostDetails.getContent());
        return blogRepository.save(existingBlogPost);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeBlog(@PathVariable Long id, Principal principal) {
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        User user = userRepository.findByUsername(principal.getName());

        // Remove from dislikedBy if present
        post.getDislikedBy().remove(user);

        // Add to likedBy
        post.getLikedBy().add(user);

        blogRepository.save(post);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislikeBlog(@PathVariable Long id, Principal principal) {
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        User user = userRepository.findByUsername(principal.getName());

        post.getLikedBy().remove(user);

        // Add to dislikedBy
        post.getDislikedBy().add(user);

        blogRepository.save(post);
        return ResponseEntity.ok().build();
    }


    // DELETE a BlogPost
    @DeleteMapping("/{id}")
    public void deleteBlogPost(@PathVariable Long id) {
        blogRepository.deleteById(id);
    }
}
