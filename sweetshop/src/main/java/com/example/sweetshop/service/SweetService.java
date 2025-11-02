package com.example.sweetshop.service;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SweetService {

    private final SweetRepository repo;

    public SweetService(SweetRepository repo) {
        this.repo = repo;
    }

    // Add new sweet
    public Sweet add(Sweet sweet) {
        return repo.save(sweet);
    }

    // List all sweets
    public List<Sweet> list() {
        return repo.findAll();
    }

    // Unified find with proper exception
    public Sweet getById(Long id) {
        return getSweetOrThrow(id);
    }

    // Flexible search with null-safe conditions
    public List<Sweet> search(String name, String category, Double minPrice, Double maxPrice) {
        if (name != null && !name.isBlank()) {
            return repo.findByNameContainingIgnoreCase(name);
        }
        if (category != null && !category.isBlank()) {
            return repo.findByCategoryIgnoreCase(category);
        }
        if (minPrice != null && maxPrice != null && minPrice <= maxPrice) {
            return repo.findByPriceBetween(minPrice, maxPrice);
        }
        return repo.findAll();
    }

    // Update sweet
    public Sweet update(Long id, Sweet payload) {
        Sweet sweet = getSweetOrThrow(id);
        sweet.setName(payload.getName());
        sweet.setCategory(payload.getCategory());
        sweet.setPrice(payload.getPrice());
        sweet.setQuantity(payload.getQuantity());
        return repo.save(sweet);
    }

    // Delete sweet
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // Customer purchase
    public Sweet purchase(Long id, int qty) {
        Sweet sweet = getSweetOrThrow(id);
        if (sweet.getQuantity() < qty) {
            throw new RuntimeException("Insufficient quantity");
        }
        sweet.setQuantity(sweet.getQuantity() - qty);
        return repo.save(sweet);
    }

    // Admin restock
    public Sweet restock(Long id, int qty) {
        Sweet sweet = getSweetOrThrow(id);
        sweet.setQuantity(sweet.getQuantity() + qty);
        return repo.save(sweet);
    }

    // Helper
    private Sweet getSweetOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found with ID: " + id));
    }
}
