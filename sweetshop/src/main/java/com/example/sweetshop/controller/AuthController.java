package com.example.sweetshop.controller;

import com.example.sweetshop.model.User;
import com.example.sweetshop.service.AuthService;
import com.example.sweetshop.security.JwtService;  // ✅ correct import

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // ✅ use your React app port
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String role = body.get("role");
        authService.register(username, password, role);
        return Map.of("message", "User registered successfully");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = authService.login(username, password);

        // ✅ generate token with both username and role
        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        return Map.of(
                "message", "Login successful",
                "username", user.getUsername(),
                "role", user.getRole(),
                "token", token // ✅ send token to frontend
        );
    }
}
