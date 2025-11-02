package com.example.sweetshop.service;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final SweetRepository repo;

    public AdminService(SweetRepository repo) {
        this.repo = repo;
    }

    public Sweet restock(Long id, int qty) {
        Sweet sweet = getSweetOrThrow(id); // ✅ use helper
        sweet.setQuantity(sweet.getQuantity() + qty);
        return repo.save(sweet);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ Helper extracted to avoid repeating lookup logic
    private Sweet getSweetOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));
    }
}
