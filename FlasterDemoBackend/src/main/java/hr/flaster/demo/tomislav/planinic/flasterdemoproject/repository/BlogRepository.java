package hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findAllByOrderByCreatedAtDesc();

    List<BlogPost> findByTitleContainingIgnoreCase(String title);

    List<BlogPost> findByAuthorId(Long authorId);

    List<BlogPost> findByAuthorUsername(String username);

    Page<BlogPost> findAll(Pageable pageable);

    Page<BlogPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    Optional<BlogPost> findByIdAndIsPublishedTrue(Long id);

    long countByAuthorId(Long authorId);

    long countByIsPublishedTrue();
}
