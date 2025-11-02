package com.example.sweetshop.service;

import com.example.sweetshop.model.User;
import com.example.sweetshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    // Constructor for Spring Boot dependency injection
    public AuthService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // Overloaded constructor for unit testing
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

    public User register(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        User saved = repo.save(user);
        logger.info("User '{}' registered successfully", user.getUsername());
        return saved;
    }

    public User login(String username, String password) {
        User user = getUserOrThrow(username);
        validatePassword(password, user.getPassword(), username);
        logger.info("User '{}' logged in successfully", username);
        return user;
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
}
