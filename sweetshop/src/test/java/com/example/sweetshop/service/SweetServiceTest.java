package com.example.sweetshop.service;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SweetServiceTest {

    @Test
    void purchaseShouldThrowWhenInsufficientQuantity() {
        SweetRepository repo = mock(SweetRepository.class);
        SweetService service = new SweetService(repo);

        Sweet s = new Sweet();
        s.setId(1L);
        s.setQuantity(2);

        when(repo.findById(1L)).thenReturn(Optional.of(s));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.purchase(1L, 5));
        assertEquals("Insufficient quantity", ex.getMessage());

        verify(repo, never()).save(any());
    }

    @Test
    void purchaseShouldReduceQuantityWhenSufficient() {
        SweetRepository repo = mock(SweetRepository.class);
        SweetService service = new SweetService(repo);

        Sweet s = new Sweet();
        s.setId(2L);
        s.setQuantity(5);

        when(repo.findById(2L)).thenReturn(Optional.of(s));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Sweet result = service.purchase(2L, 3);
        assertEquals(2, result.getQuantity());
        verify(repo).save(any());
    }
}
