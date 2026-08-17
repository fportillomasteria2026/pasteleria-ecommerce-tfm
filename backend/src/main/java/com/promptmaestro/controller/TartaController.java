package com.promptmaestro.controller;

import com.promptmaestro.entity.Tarta;
import com.promptmaestro.repository.TartaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tartas")
public class TartaController {

    private final TartaRepository repository;

    public TartaController(TartaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tarta> getAll() {
        return repository.findByActivoTrue();
    }

    @GetMapping("/search")
    public List<Tarta> search(@RequestParam String q) {
        return repository.findByNombreContainingIgnoreCaseAndActivoTrue(q);
    }

    @GetMapping("/tamano/{tamano}")
    public List<Tarta> getByTamano(@PathVariable String tamano) {
        return repository.findByTamanoAndActivoTrue(tamano);
    }

    @GetMapping("/forma/{forma}")
    public List<Tarta> getByForma(@PathVariable String forma) {
        return repository.findByFormaAndActivoTrue(forma);
    }

    @PostMapping
    public Tarta create(@RequestBody Tarta tarta) {
        return repository.save(tarta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarta> update(@PathVariable Long id, @RequestBody Tarta updated) {
        return repository.findById(id).map(t -> {
            t.setSku(updated.getSku());
            t.setNombre(updated.getNombre());
            t.setDescripcion(updated.getDescripcion());
            t.setImagenUrl(updated.getImagenUrl());
            t.setTamano(updated.getTamano());
            t.setPisos(updated.getPisos());
            t.setForma(updated.getForma());
            t.setDimensiones(updated.getDimensiones());
            t.setSaborBizcocho(updated.getSaborBizcocho());
            t.setFrutas(updated.getFrutas());
            t.setTipoCrema(updated.getTipoCrema());
            t.setTipoPersonalizacion(updated.getTipoPersonalizacion());
            t.setPrecioPublico(updated.getPrecioPublico());
            t.setCoste(updated.getCoste());
            t.setDisponible(updated.getDisponible());
            t.setNotas(updated.getNotas());
            return ResponseEntity.ok(repository.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repository.findById(id).map(t -> {
            t.setActivo(false);
            repository.save(t);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
