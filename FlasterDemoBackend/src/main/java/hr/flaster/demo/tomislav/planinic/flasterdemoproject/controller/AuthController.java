package hr.flaster.demo.tomislav.planinic.flasterdemoproject.controller;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.dto.LoginRequest;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.dto.RegisterRequest;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.Role;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.entity.User;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.RoleRepository;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles authentication and user registration operations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Constructs an {@code AuthController} with dependencies for authentication and user management.
     *
     * @param authenticationManager AuthenticationManager for login authentication.
     * @param userRepository Repository for user persistence.
     * @param passwordEncoder Password encoder for secure password storage.
     * @param roleRepository Repository for retrieving user roles.
     */
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Handles user registration.
     *
     * @param registerRequest Contains user registration details (username, email, password, role).
     * @return Response indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Assign default role
        Role defaultRole = roleRepository.findByName("ROLE_READER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.getRoles().add(defaultRole);

        // Assign AUTHOR role if requested
        if ("AUTHOR".equals(registerRequest.getRole())) {
            Role authorRole = roleRepository.findByName("ROLE_AUTHOR")
                    .orElseThrow(() -> new RuntimeException("Author role not found"));
            user.getRoles().add(authorRole);
        }

        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully!"));
    }

    /**
     * Handles user login and JWT token generation.
     *
     * @param loginRequest Contains login credentials (username, password).
     * @return Response with JWT token if authentication is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Generate JWT token upon successful login
            String token = generateJwtToken(loginRequest.getUsername());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    /**
     * Generates a JWT token containing user details.
     *
     * @param username Username for which to generate the token.
     * @return Generated JWT token.
     */
    private String generateJwtToken(String username) {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        List<String> roleNames = userEntity.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
