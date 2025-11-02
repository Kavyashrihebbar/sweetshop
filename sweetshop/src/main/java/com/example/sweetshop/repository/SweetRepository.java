package com.example.sweetshop.repository;
import com.example.sweetshop.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SweetRepository extends JpaRepository<Sweet, Long> {
    List<Sweet> findByNameContainingIgnoreCase(String name);
    List<Sweet> findByCategoryIgnoreCase(String category);
    List<Sweet> findByPriceBetween(double min, double max);
}
