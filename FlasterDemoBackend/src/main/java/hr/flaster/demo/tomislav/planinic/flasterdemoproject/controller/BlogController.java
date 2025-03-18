package hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.BlogPost;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.User;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.BlogRepository;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller for managing blog posts.
 */
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a {@code BlogController} with repositories for blog and user management.
     *
     * @param blogRepository Repository for blog post persistence.
     * @param userRepository Repository for user persistence.
     */
    public BlogController(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all blog posts.
     *
     * @return A list of all blog posts.
     */
    @GetMapping
    public List<BlogPost> getAllBlogPosts() {
        return blogRepository.findAll();
    }

    /**
     * Retrieves a blog post by its ID.
     *
     * @param id The blog post ID.
     * @return The requested blog post.
     */
    @GetMapping("/{id}")
    public BlogPost getBlogPostById(@PathVariable Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));
    }

    /**
     * Creates a new blog post.
     *
     * @param blogPost The blog post to create.
     * @param principal The authenticated user creating the post.
     * @return The created blog post.
     */
    @PostMapping
    public BlogPost createBlogPost(@RequestBody BlogPost blogPost, Principal principal) {
        User author = userRepository.findByUsername(principal.getName());
        blogPost.setAuthor(author);
        return blogRepository.save(blogPost);
    }

    /**
     * Updates an existing blog post.
     *
     * @param id The ID of the blog post to update.
     * @param blogPostDetails The updated blog post details.
     * @return The updated blog post.
     */
    @PutMapping("/{id}")
    public BlogPost updateBlogPost(@PathVariable Long id, @RequestBody BlogPost blogPostDetails) {
        BlogPost existingBlogPost = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));

        existingBlogPost.setTitle(blogPostDetails.getTitle());
        existingBlogPost.setContent(blogPostDetails.getContent());
        return blogRepository.save(existingBlogPost);
    }

    /**
     * Likes a blog post.
     *
     * @param id The ID of the blog post to like.
     * @param principal The authenticated user performing the action.
     * @return Response entity indicating success.
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeBlog(@PathVariable Long id, Principal principal) {
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        User user = userRepository.findByUsername(principal.getName());

        post.getDislikedBy().remove(user);
        post.getLikedBy().add(user);

        blogRepository.save(post);
        return ResponseEntity.ok().build();
    }

    /**
     * Dislikes a blog post.
     *
     * @param id The ID of the blog post to dislike.
     * @param principal The authenticated user performing the action.
     * @return Response entity indicating success.
     */
    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislikeBlog(@PathVariable Long id, Principal principal) {
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        User user = userRepository.findByUsername(principal.getName());

        post.getLikedBy().remove(user);
        post.getDislikedBy().add(user);

        blogRepository.save(post);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a blog post.
     *
     * @param id The ID of the blog post to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteBlogPost(@PathVariable Long id) {
        blogRepository.deleteById(id);
    }
}
