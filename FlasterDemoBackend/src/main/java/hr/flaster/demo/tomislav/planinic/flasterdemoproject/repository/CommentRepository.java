package hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}