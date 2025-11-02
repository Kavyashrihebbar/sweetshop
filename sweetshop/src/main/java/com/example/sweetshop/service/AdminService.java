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
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");

        Sweet sweet = getSweetOrThrow(id); // find the sweet
        sweet.setQuantity(sweet.getQuantity() + qty); // increase quantity
        return repo.save(sweet); // save and return updated sweet
    }

    private Sweet getSweetOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    
}
