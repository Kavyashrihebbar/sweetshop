package com.example.sweetshop.service;

import com.example.sweetshop.model.User;
import com.example.sweetshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // Constructor for Spring Boot dependency injection
    @Autowired
    public AuthService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // Overloaded constructor for unit testing (no Spring context)
    public AuthService(UserRepository repo) {
        this.repo = repo;
        this.encoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    // --------------------------
    // Helper Methods
    // --------------------------

    private User getUserOrThrow(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed: user '{}' not found", username);
                    return new UserNotFoundException("User not found: " + username);
                });
    }

    private void validatePassword(String rawPassword, String encodedPassword, String username) {
        if (!encoder.matches(rawPassword, encodedPassword)) {
            logger.warn("Invalid password attempt for user '{}'", username);
            throw new InvalidPasswordException("Invalid password for user: " + username);
        }
    }

    private String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // --------------------------
    // Public Methods
    // --------------------------

    public User register(String username, String password, String role) {
        if (repo.findByUsername(username).isPresent()) {
            logger.warn("Registration failed: username '{}' already exists", username);
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        user.setRole(role);

        User saved = repo.save(user);
        logger.info("User '{}' registered successfully with role '{}'", username, role);
        return saved; // ✅ return the saved user
    }

    public User login(String username, String password) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user; // ✅ return user
    }

    // --------------------------
    // Custom Exceptions (Inner Classes)
    // --------------------------

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }
}
