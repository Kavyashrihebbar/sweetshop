package com.example.sweetshop.service;
import com.example.sweetshop.model.User;
import com.example.sweetshop.repository.UserRepository;
import com.example.sweetshop.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtService jwtService){
        this.userRepo=userRepo; this.encoder=encoder; this.jwtService=jwtService;
    }

    public String register(String username, String password, String role) {
        if (userRepo.findByUsername(username).isPresent()) throw new RuntimeException("User exists");
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole(role == null ? "USER" : role);
        userRepo.save(u);
        return jwtService.generateToken(u.getUsername(), u.getRole());
    }

    public String login(String username, String password) {
        User u = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid creds"));
        if (!encoder.matches(password, u.getPassword())) throw new RuntimeException("Invalid creds");
        return jwtService.generateToken(u.getUsername(), u.getRole());
    }
}
