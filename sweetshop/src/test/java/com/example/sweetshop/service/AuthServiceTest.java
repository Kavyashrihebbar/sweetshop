package com.example.sweetshop.service;

import com.example.sweetshop.model.User;
import com.example.sweetshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository repo = mock(UserRepository.class);
    private final AuthService service = new AuthService(repo);

    @Test
    void register_shouldSaveNewUser() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("pass");

        when(repo.save(any(User.class))).thenReturn(user);

        User saved = service.register(user);
        assertEquals("alice", saved.getUsername());
        verify(repo).save(user);
    }

    @Test
    void login_shouldThrowWhenInvalid() {
        when(repo.findByUsername("alice")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> service.login("alice", "wrong"));
    }
}
