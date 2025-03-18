package hr.flaster.demo.tomislav.planinic.flasterdemoproject.config;

import hr.flaster.demo.tomislav.planinic.flasterdemoproject.security.JwtAuthEntryPoint;
import hr.flaster.demo.tomislav.planinic.flasterdemoproject.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures security settings for the application, including JWT authentication,
 * role-based access control, and password encoding.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    /**
     * Configures security filter chain, defining authorization rules and authentication mechanisms.
     *
     * @param http HttpSecurity instance to configure security settings.
     * @return Configured {@link SecurityFilterChain}.
     * @throws Exception if any error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/blogs").hasRole("AUTHOR")
                .requestMatchers(HttpMethod.PUT, "/api/blogs/**").hasRole("AUTHOR")
                .requestMatchers(HttpMethod.DELETE, "/api/blogs/**").hasRole("AUTHOR")
                .requestMatchers(HttpMethod.GET, "/api/blogs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/blogs/{id}/like").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/blogs/{id}/dislike").authenticated()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthEntryPoint()));

        return http.build();
    }

    /**
     * Provides an {@link AuthenticationManager} for authentication purposes.
     *
     * @param config AuthenticationConfiguration to retrieve AuthenticationManager.
     * @return Configured {@link AuthenticationManager}.
     * @throws Exception if retrieval fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a password encoder using BCrypt hashing.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures DAO-based authentication using {@link MyUserDetailsService} and password encoding.
     *
     * @return Configured {@link DaoAuthenticationProvider}.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures and provides an {@link AuthenticationManager} with DAO authentication.
     *
     * @param http HttpSecurity instance for authentication setup.
     * @return Configured {@link AuthenticationManager}.
     * @throws Exception if configuration fails.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthProvider())
                .build();
    }
}
