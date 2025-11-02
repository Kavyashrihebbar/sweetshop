package com.example.sweetshop.service;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SweetService {
    private final SweetRepository repo;

    public SweetService(SweetRepository repo) {
        this.repo = repo;
    }

    public Sweet add(Sweet s) {
        return repo.save(s);
    }

    public List<Sweet> list() {
        return repo.findAll();
    }

    public Optional<Sweet> findById(Long id) {
        return repo.findById(id);
    }

    public List<Sweet> search(String name, String category, Double minPrice, Double maxPrice) {
        if (name != null) return repo.findByNameContainingIgnoreCase(name);
        if (category != null) return repo.findByCategoryIgnoreCase(category);
        if (minPrice != null && maxPrice != null) return repo.findByPriceBetween(minPrice, maxPrice);
        return repo.findAll();
    }

    public Sweet update(Long id, Sweet payload) {
        Sweet s = getSweetOrThrow(id);
        s.setName(payload.getName());
        s.setCategory(payload.getCategory());
        s.setPrice(payload.getPrice());
        s.setQuantity(payload.getQuantity());
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Sweet purchase(Long id, int qty) {
        Sweet s = getSweetOrThrow(id);
        if (s.getQuantity() < qty)
            throw new RuntimeException("Insufficient quantity");
        s.setQuantity(s.getQuantity() - qty);
        return repo.save(s);
    }

    public Sweet restock(Long id, int qty) {
        Sweet s = getSweetOrThrow(id);
        s.setQuantity(s.getQuantity() + qty);
        return repo.save(s);
    }

    // ✅ Your new helper method goes here
    private Sweet getSweetOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found"));
    }
}
