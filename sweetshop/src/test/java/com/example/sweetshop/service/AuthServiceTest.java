package com.example.sweetshop.service;

import com.example.sweetshop.model.User;
import com.example.sweetshop.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository repo = mock(UserRepository.class);
    private final AuthService service = new AuthService(repo);

    @Test
    void register_shouldSaveNewUser() {
        // Arrange
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass");
        user.setRole("USER");

        when(repo.findByUsername("alice")).thenReturn(Optional.empty());
        when(repo.save(any(User.class))).thenReturn(user);

        // Act
        User saved = service.register("alice", "pass", "USER"); // ✅ fixed method

        // Assert
        assertEquals("alice", saved.getUsername());
        verify(repo).save(any(User.class));
    }

    @Test
    void login_shouldThrowWhenInvalid() {
        when(repo.findByUsername("alice")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.login("alice", "wrong"));
    }
}
