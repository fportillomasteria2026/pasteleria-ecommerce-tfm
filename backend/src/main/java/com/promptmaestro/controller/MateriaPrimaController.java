package com.promptmaestro.controller;

import com.promptmaestro.entity.MateriaPrima;
import com.promptmaestro.service.SupabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/materia-prima")
public class MateriaPrimaController {

    private final SupabaseService supabaseService;

    public MateriaPrimaController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @GetMapping
    public List<MateriaPrima> getAll() {
        return supabaseService.getAll();
    }

    @PostMapping
    public MateriaPrima create(@RequestBody MateriaPrima item) {
        return supabaseService.create(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrima> update(@PathVariable Long id, @RequestBody MateriaPrima item) {
        return supabaseService.update(id, item)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supabaseService.delete(id);
        return ResponseEntity.ok().build();
    }
}
