package com.example.sweetshop.service;

import com.example.sweetshop.model.User;
import com.example.sweetshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public User login(String username, String password) {
    User user = repo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!encoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    return user;
}

}
