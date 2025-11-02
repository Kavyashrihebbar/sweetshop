package com.example.sweetshop.controller;
import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.service.SweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sweets")
@CrossOrigin(origins = "http://localhost:3000")
public class SweetController {
    private final SweetService service;
    public SweetController(SweetService service){this.service=service;}

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Sweet s, Authentication auth){
        return ResponseEntity.ok(service.add(s));
    }

    @GetMapping
    public List<Sweet> list(){ return service.list(); }

    @GetMapping("/search")
    public List<Sweet> search(@RequestParam(required=false) String name,
                              @RequestParam(required=false) String category,
                              @RequestParam(required=false) Double minPrice,
                              @RequestParam(required=false) Double maxPrice) {
        return service.search(name, category, minPrice, maxPrice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Sweet s, Authentication auth){
        return ResponseEntity.ok(service.update(id, s));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth){
        if (!hasRole(auth, "ADMIN")) return ResponseEntity.status(403).body("Forbidden");
        service.delete(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<?> purchase(@PathVariable Long id, @RequestBody Map<String,Integer> body){
        int qty = body.getOrDefault("quantity",1);
        return ResponseEntity.ok(service.purchase(id, qty));
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<?> restock(@PathVariable Long id, @RequestBody Map<String,Integer> body, Authentication auth){
        if (!hasRole(auth, "ADMIN")) return ResponseEntity.status(403).body("Forbidden");
        int qty = body.getOrDefault("quantity",1);
        return ResponseEntity.ok(service.restock(id, qty));
    }

    private boolean hasRole(Authentication auth, String role){
        if (auth==null) return false;
        return auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_" + role));
    }
}
