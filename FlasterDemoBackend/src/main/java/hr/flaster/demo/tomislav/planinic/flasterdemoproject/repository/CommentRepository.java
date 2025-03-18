package hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBlogPostId(Long blogId);
}