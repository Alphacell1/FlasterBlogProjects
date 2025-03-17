package hr.flaster.demo.tomislav.planinic.flasterdemoproject.config;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Role;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // If the roles don’t exist, create them
            if (roleRepository.findByName("ROLE_READER").isEmpty()) {
                roleRepository.save(new Role("ROLE_READER"));
            }
            if (roleRepository.findByName("ROLE_AUTHOR").isEmpty()) {
                roleRepository.save(new Role("ROLE_AUTHOR"));
            }
            // Add more roles if needed
        };
    }
}
