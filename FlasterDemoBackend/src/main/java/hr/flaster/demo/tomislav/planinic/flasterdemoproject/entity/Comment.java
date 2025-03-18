package hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a comment made on a blog post.
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "roles"}) // Prevents serialization of sensitive data
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    @JsonIgnoreProperties({"author", "likedBy", "comments"}) // Prevents cyclic references
    private BlogPost blogPost;

    /**
     * Default constructor.
     */
    public Comment() {}

    /**
     * Sets the creation timestamp before persisting the entity.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets the comment ID.
     *
     * @return The ID of the comment.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the comment ID.
     *
     * @param id The ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the message content of the comment.
     *
     * @return The comment text.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of the comment.
     *
     * @param message The comment text to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the creation timestamp of the comment.
     *
     * @return The date and time when the comment was created.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the comment.
     *
     * @param createdAt The timestamp to set.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the blog post associated with the comment.
     *
     * @return The blog post to which this comment belongs.
     */
    public BlogPost getBlogPost() {
        return blogPost;
    }

    /**
     * Sets the blog post associated with the comment.
     *
     * @param blogPost The blog post to associate with this comment.
     */
    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    /**
     * Gets the user who made the comment.
     *
     * @return The user who authored the comment.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who made the comment.
     *
     * @param user The user to associate with this comment.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
