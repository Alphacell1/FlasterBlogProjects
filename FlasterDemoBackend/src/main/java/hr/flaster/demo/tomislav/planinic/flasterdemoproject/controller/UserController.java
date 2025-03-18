package hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Role;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.User;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        // Return a simple DTO or safe data
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", user.getUsername());
        profile.put("email", user.getEmail());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        profile.put("roles", roles);
        return ResponseEntity.ok(profile);
    }
}
