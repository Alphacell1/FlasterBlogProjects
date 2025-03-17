package hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}