package com.example.sweetshop.controller;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.service.SweetService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SweetControllerTest {
    SweetService service = mock(SweetService.class);
    SweetController controller = new SweetController(service);

    @Test
    void search_shouldReturnFilteredList() {
        when(service.search("laddu", null, null, null))
            .thenReturn(List.of(new Sweet()));
        ResponseEntity<List<Sweet>> response = controller.search("laddu", null, null, null);
        assertEquals(200, response.getStatusCodeValue());
        verify(service).search("laddu", null, null, null);
    }
}
