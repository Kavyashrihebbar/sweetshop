package com.example.sweetshop.service;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {
    SweetRepository repo = mock(SweetRepository.class);
    AdminService service = new AdminService(repo);

    @Test
    void restock_shouldIncreaseQuantity() {
        Sweet s = new Sweet();
        s.setQuantity(5); // ✅ int, not long
        when(repo.findById(1L)).thenReturn(java.util.Optional.of(s));
        when(repo.save(s)).thenReturn(s);

        Sweet result = service.restock(1L, 10);
        assertEquals(15, result.getQuantity()); // ✅ also int
    }

    @Test
    void delete_shouldCallRepoDelete() {
        service.delete(1L);
        verify(repo).deleteById(1L);
    }
}  