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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // 1) Register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 1) Assign roles to user
        Role defaultRole = roleRepository.findByName("ROLE_READER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.getRoles().add(defaultRole);

        // 2) If the user wants to be an author, optionally do:
        if (registerRequest.getRole() != null && registerRequest.getRole().equals("AUTHOR")) {
            Role authorRole = roleRepository.findByName("ROLE_AUTHOR")
                    .orElseThrow(() -> new RuntimeException("Author role not found"));
            user.getRoles().add(authorRole);
        }

        userRepository.save(user);

        // Return JSON instead of plain text
        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully!"));
    }


    // 2) Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            System.out.println("LOGIN MY MATEY");
            // If successful, generate JWT
            String token = generateJwtToken(loginRequest.getUsername());
            // Return token in a JSON object
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    private String generateJwtToken(String username) {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        // Convert userEntity.getRoles() to a list of role names
        List<String> roleNames = userEntity.getRoles().stream()
                .map(Role::getName)  // e.g. "ROLE_AUTHOR"
                .collect(Collectors.toList());

        long expirationMs = 24 * 60 * 60 * 1000; // 24h
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)  // <--- put roles in the token
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

}
