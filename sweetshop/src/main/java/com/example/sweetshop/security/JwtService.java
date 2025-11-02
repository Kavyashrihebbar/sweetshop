package com.example.sweetshop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) { return false; }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(secret.getBytes())
                   .build().parseClaimsJws(token).getBody().getSubject();
    }

    public String extractRole(String token) {
        Object r = Jwts.parserBuilder().setSigningKey(secret.getBytes())
                  .build().parseClaimsJws(token).getBody().get("role");
        return r==null ? null : r.toString();
    }
}
