package hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing blog posts.
 */
@Repository
public interface BlogRepository extends JpaRepository<BlogPost, Long> {

    /**
     * Retrieves all blog posts ordered by creation date in descending order.
     *
     * @return A list of blog posts ordered by the latest first.
     */
    List<BlogPost> findAllByOrderByCreatedAtDesc();

    /**
     * Searches for blog posts containing the given title (case-insensitive).
     *
     * @param title The title to search for.
     * @return A list of matching blog posts.
     */
    List<BlogPost> findByTitleContainingIgnoreCase(String title);

    /**
     * Retrieves all blog posts created by a specific author.
     *
     * @param authorId The ID of the author.
     * @return A list of blog posts by the author.
     */
    List<BlogPost> findByAuthorId(Long authorId);

    /**
     * Retrieves all blog posts created by a specific author using their username.
     *
     * @param username The username of the author.
     * @return A list of blog posts by the specified author.
     */
    List<BlogPost> findByAuthorUsername(String username);

    /**
     * Retrieves paginated blog posts.
     *
     * @param pageable The pagination details.
     * @return A paginated list of blog posts.
     */
    Page<BlogPost> findAll(Pageable pageable);

    /**
     * Searches for blog posts where the title or content contains the given search term (case-insensitive).
     *
     * @param title   The title search term.
     * @param content The content search term.
     * @param pageable The pagination details.
     * @return A paginated list of matching blog posts.
     */
    Page<BlogPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    /**
     * Retrieves a blog post by its ID only if it is published.
     *
     * @param id The ID of the blog post.
     * @return An optional containing the blog post if it is published.
     */
    Optional<BlogPost> findByIdAndIsPublishedTrue(Long id);

    /**
     * Counts the number of blog posts created by a specific author.
     *
     * @param authorId The ID of the author.
     * @return The count of blog posts by the author.
     */
    long countByAuthorId(Long authorId);

    /**
     * Counts the number of published blog posts.
     *
     * @return The count of published blog posts.
     */
    long countByIsPublishedTrue();
}
