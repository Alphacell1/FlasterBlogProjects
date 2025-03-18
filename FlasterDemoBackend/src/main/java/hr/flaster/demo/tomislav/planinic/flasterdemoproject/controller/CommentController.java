package hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.BlogPost;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Comment;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.User;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.BlogRepository;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.CommentRepository;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing comments on blog posts.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a {@code CommentController} with repositories for comment, blog, and user management.
     *
     * @param commentRepository Repository for comment persistence.
     * @param blogRepository Repository for blog post persistence.
     * @param userRepository Repository for user persistence.
     */
    public CommentController(CommentRepository commentRepository,
                             BlogRepository blogRepository,
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds a comment to a blog post.
     *
     * @param blogId The ID of the blog post to comment on.
     * @param payload The comment message.
     * @param principal The authenticated user making the comment.
     * @return The created comment.
     */
    @PostMapping("/{blogId}")
    public Comment addComment(@PathVariable Long blogId,
                              @RequestBody Map<String, String> payload,
                              Principal principal) {
        String message = payload.get("message");
        BlogPost blogPost = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Comment comment = new Comment();
        comment.setBlogPost(blogPost);
        comment.setUser(user);
        comment.setMessage(message);
        return commentRepository.save(comment);
    }

    /**
     * Retrieves all comments for a given blog post.
     *
     * @param blogId The ID of the blog post.
     * @return A list of comments for the specified blog post.
     */
    @GetMapping("/{blogId}")
    public List<Comment> getComments(@PathVariable Long blogId) {
        return commentRepository.findAllByBlogPostId(blogId);
    }
}
