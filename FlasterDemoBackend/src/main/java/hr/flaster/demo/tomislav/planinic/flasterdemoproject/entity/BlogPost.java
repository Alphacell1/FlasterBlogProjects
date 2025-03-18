package hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a blog post entity in the system.
 */
@Entity
@Table(name = "blog_posts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"roles", "password", "email"}) // Prevents cyclic references
    private User author;

    @Column(nullable = false)
    private boolean isPublished = false;

    @ManyToMany
    @JoinTable(name = "blog_likes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedBy = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "blog_dislikes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> dislikedBy = new HashSet<>();

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    /**
     * Default constructor.
     */
    public BlogPost() {}

    /**
     * Sets the created timestamp before persisting the entity.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Updates the timestamp before modifying the entity.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Gets the blog post ID.
     *
     * @return The ID of the blog post.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the blog post ID.
     *
     * @param id The ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the blog post title.
     *
     * @return The title of the blog post.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the blog post title.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the blog post content.
     *
     * @return The content of the blog post.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the blog post content.
     *
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return The date and time when the blog post was created.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt The timestamp to set.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return The date and time when the blog post was last updated.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt The timestamp to set.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the author of the blog post.
     *
     * @return The {@link User} who authored the blog post.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Sets the author of the blog post.
     *
     * @param author The {@link User} who wrote the blog post.
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Gets the list of users who liked the blog post.
     *
     * @return A set of users who liked the post.
     */
    public Set<User> getLikedBy() {
        return likedBy;
    }

    /**
     * Sets the list of users who liked the blog post.
     *
     * @param likedBy A set of users who liked the post.
     */
    public void setLikedBy(Set<User> likedBy) {
        this.likedBy = likedBy;
    }

    /**
     * Gets the list of users who disliked the blog post.
     *
     * @return A set of users who disliked the post.
     */
    public Set<User> getDislikedBy() {
        return dislikedBy;
    }

    /**
     * Sets the list of users who disliked the blog post.
     *
     * @param dislikedBy A set of users who disliked the post.
     */
    public void setDislikedBy(Set<User> dislikedBy) {
        this.dislikedBy = dislikedBy;
    }

    /**
     * Gets the comments associated with the blog post.
     *
     * @return A set of comments for the post.
     */
    public Set<Comment> getComments() {
        return comments;
    }

    /**
     * Sets the comments for the blog post.
     *
     * @param comments A set of comments to associate with the post.
     */
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Checks if the blog post is published.
     *
     * @return {@code true} if the blog post is published, otherwise {@code false}.
     */
    public boolean isPublished() {
        return isPublished;
    }

    /**
     * Sets the published status of the blog post.
     *
     * @param isPublished {@code true} to mark the post as published, otherwise {@code false}.
     */
    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }
}
