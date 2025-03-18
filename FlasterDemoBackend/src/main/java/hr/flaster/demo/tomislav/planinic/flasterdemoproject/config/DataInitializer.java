package hr.flaster.demo.tomislav.planinic.flasterdemoproject.config;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Role;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Initializes default roles in the database when the application starts.
 */
@Configuration
public class DataInitializer {

    /**
     * Ensures that required roles (READER and AUTHOR) exist in the database.
     *
     * @param roleRepository Repository for role persistence.
     * @return A CommandLineRunner that checks and creates roles if they do not exist.
     */
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_READER").isEmpty()) {
                roleRepository.save(new Role("ROLE_READER"));
            }
            if (roleRepository.findByName("ROLE_AUTHOR").isEmpty()) {
                roleRepository.save(new Role("ROLE_AUTHOR"));
            }
        };
    }
}
