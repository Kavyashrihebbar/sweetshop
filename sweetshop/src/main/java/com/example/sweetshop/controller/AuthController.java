package com.example.sweetshop.controller;

import com.example.sweetshop.model.User;
import com.example.sweetshop.service.AuthService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        User user = new User();
        user.setUsername(body.get("username"));
        user.setPassword(body.get("password"));
        user.setRole(body.get("role"));

        auth.register(user); // ✅ matches AuthService
        return Map.of("message", "User registered successfully");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        auth.login(body.get("username"), body.get("password")); // ✅ already exists
        return Map.of("message", "Login successful");
    }
}
