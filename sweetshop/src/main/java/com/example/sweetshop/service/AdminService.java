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
        Sweet s = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        s.setQuantity(s.getQuantity() + qty);
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
