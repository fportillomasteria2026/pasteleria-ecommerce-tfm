package com.promptmaestro.controller;

import com.promptmaestro.entity.Tarta;
import com.promptmaestro.repository.TartaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TartaController {

    private final TartaRepository tartaRepository;

    public TartaController(TartaRepository tartaRepository) {
        this.tartaRepository = tartaRepository;
    }

    // Public endpoints
    @GetMapping("/tartas")
    public List<Tarta> getAllTartas() { return tartaRepository.findByActivoTrue(); }

    @GetMapping("/tartas/search")
    public List<Tarta> searchTartas(@RequestParam String q) {
        return tartaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(q);
    }

    // Admin endpoints
    @GetMapping("/admin/tartas")
    public List<Tarta> getAll() { return tartaRepository.findByActivoTrue(); }

    @GetMapping("/admin/tartas/search")
    public List<Tarta> search(@RequestParam String q) {
        return tartaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(q);
    }

    @PostMapping("/admin/tartas")
    public Tarta create(@RequestBody Tarta tarta) {
        return tartaRepository.save(tarta);
    }

    @PutMapping("/admin/tartas/{id}")
    public ResponseEntity<Tarta> update(@PathVariable Long id, @RequestBody Tarta updated) {
        return tartaRepository.findById(id).map(t -> {
            t.setSku(updated.getSku());
            t.setNombre(updated.getNombre());
            t.setDescripcion(updated.getDescripcion());
            t.setImagenUrl(updated.getImagenUrl());
            t.setHashtags(updated.getHashtags());
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
            return ResponseEntity.ok(tartaRepository.save(t));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/tartas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tartaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
