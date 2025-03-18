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

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository,
                             BlogRepository blogRepository,
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    // Add comment to a blog post
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

    // Optionally, get comments for a blog post
    @GetMapping("/{blogId}")
    public List<Comment> getComments(@PathVariable Long blogId) {
        // or create a custom query "findByBlogPostId(blogId)"
        return commentRepository.findAllByBlogPostId(blogId);
    }
}

