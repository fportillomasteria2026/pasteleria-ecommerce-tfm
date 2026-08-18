package com.promptmaestro.controller;

import com.promptmaestro.entity.MateriaPrima;
import com.promptmaestro.repository.MateriaPrimaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/materia-prima")
public class MateriaPrimaController {

    private final MateriaPrimaRepository repository;

    public MateriaPrimaController(MateriaPrimaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MateriaPrima> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public MateriaPrima create(@RequestBody MateriaPrima item) {
        return repository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrima> update(@PathVariable Long id, @RequestBody MateriaPrima updated) {
        return repository.findById(id).map(item -> {
            item.setCodigoSku(updated.getCodigoSku());
            item.setNombre(updated.getNombre());
            item.setMarca(updated.getMarca());
            item.setProveedor(updated.getProveedor());
            item.setCoste(updated.getCoste());
            item.setFormato(updated.getFormato());
            item.setPeso(updated.getPeso());
            item.setUnidad(updated.getUnidad());
            item.setCantidad(updated.getCantidad());
            return ResponseEntity.ok(repository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
