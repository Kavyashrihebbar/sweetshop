package com.example.sweetshop.controller;
import com.example.sweetshop.service.AuthService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth){this.auth=auth;}

    @PostMapping("/register")
    public Map<String,String> register(@RequestBody Map<String,String> body){
        String token = auth.register(body.get("username"), body.get("password"), body.get("role"));
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> body){
        String token = auth.login(body.get("username"), body.get("password"));
        return Map.of("token", token);
    }
}
