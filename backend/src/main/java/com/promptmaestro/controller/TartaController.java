package com.promptmaestro.controller;

import com.promptmaestro.entity.Hashtag;
import com.promptmaestro.entity.Tarta;
import com.promptmaestro.repository.HashtagRepository;
import com.promptmaestro.repository.TartaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TartaController {

    private final TartaRepository tartaRepository;
    private final HashtagRepository hashtagRepository;

    public TartaController(TartaRepository tartaRepository, HashtagRepository hashtagRepository) {
        this.tartaRepository = tartaRepository;
        this.hashtagRepository = hashtagRepository;
    }

    // Public endpoints for gallery
    @GetMapping("/tartas")
    public List<Map<String, Object>> getAllTartas() {
        return tartaRepository.findByActivoTrue().stream().map(this::toMap).collect(Collectors.toList());
    }

    @GetMapping("/tartas/search")
    public List<Map<String, Object>> searchTartas(@RequestParam String q) {
        return tartaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(q).stream().map(this::toMap).collect(Collectors.toList());
    }

    @GetMapping("/tartas/by-hashtags")
    public List<Map<String, Object>> getByHashtags(@RequestParam List<String> tags) {
        Set<String> tagSet = new HashSet<>(tags);
        return tartaRepository.findByActivoTrue().stream()
                .filter(t -> t.getHashtags().stream().anyMatch(h -> tagSet.contains(h.getName())))
                .map(this::toMap).collect(Collectors.toList());
    }

    // Admin endpoints
    @GetMapping("/admin/tartas")
    public List<Map<String, Object>> getAll() { return tartaRepository.findByActivoTrue().stream().map(this::toMap).collect(Collectors.toList()); }

    @GetMapping("/admin/tartas/search")
    public List<Map<String, Object>> search(@RequestParam String q) {
        return tartaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(q).stream().map(this::toMap).collect(Collectors.toList());
    }

    @PostMapping("/admin/tartas")
    public Map<String, Object> create(@RequestBody Map<String, Object> body) {
        Tarta tarta = mapToEntity(body);
        addHashtags(tarta, body);
        return toMap(tartaRepository.save(tarta));
    }

    @PutMapping("/admin/tartas/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return tartaRepository.findById(id).map(tarta -> {
            updateEntity(tarta, body);
            if (body.containsKey("hashtags")) {
                tarta.getHashtags().clear();
                addHashtags(tarta, body);
            }
            return ResponseEntity.ok(toMap(tartaRepository.save(tarta)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/tartas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return tartaRepository.findById(id).map(t -> {
            t.setActivo(false);
            tartaRepository.save(t);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private void addHashtags(Tarta tarta, Map<String, Object> body) {
        if (body.containsKey("hashtags")) {
            @SuppressWarnings("unchecked")
            List<String> tagNames = (List<String>) body.get("hashtags");
            for (String tagName : tagNames) {
                Hashtag hashtag = hashtagRepository.findByName(tagName)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tagName).build()));
                tarta.getHashtags().add(hashtag);
            }
        }
    }

    private Map<String, Object> toMap(Tarta t) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", t.getId());
        map.put("sku", t.getSku());
        map.put("nombre", t.getNombre());
        map.put("descripcion", t.getDescripcion());
        map.put("imagenUrl", t.getImagenUrl());
        map.put("hashtags", t.getHashtags().stream().map(Hashtag::getName).collect(Collectors.toList()));
        map.put("tamano", t.getTamano());
        map.put("pisos", t.getPisos());
        map.put("forma", t.getForma());
        map.put("dimensiones", t.getDimensiones());
        map.put("saborBizcocho", t.getSaborBizcocho());
        map.put("frutas", t.getFrutas());
        map.put("tipoCrema", t.getTipoCrema());
        map.put("tipoPersonalizacion", t.getTipoPersonalizacion());
        map.put("precioPublico", t.getPrecioPublico());
        map.put("coste", t.getCoste());
        map.put("disponible", t.getDisponible());
        map.put("notas", t.getNotas());
        return map;
    }

    private Tarta mapToEntity(Map<String, Object> body) {
        Tarta t = new Tarta();
        updateEntity(t, body);
        return t;
    }

    private void updateEntity(Tarta t, Map<String, Object> body) {
        if (body.containsKey("sku")) t.setSku((String) body.get("sku"));
        if (body.containsKey("nombre")) t.setNombre((String) body.get("nombre"));
        if (body.containsKey("descripcion")) t.setDescripcion((String) body.get("descripcion"));
        if (body.containsKey("imagenUrl")) t.setImagenUrl((String) body.get("imagenUrl"));
        if (body.containsKey("tamano")) t.setTamano((String) body.get("tamano"));
        if (body.containsKey("pisos")) t.setPisos((Integer) body.get("pisos"));
        if (body.containsKey("forma")) t.setForma((String) body.get("forma"));
        if (body.containsKey("dimensiones")) t.setDimensiones((String) body.get("dimensiones"));
        if (body.containsKey("saborBizcocho")) t.setSaborBizcocho((String) body.get("saborBizcocho"));
        if (body.containsKey("frutas")) t.setFrutas((String) body.get("frutas"));
        if (body.containsKey("tipoCrema")) t.setTipoCrema((String) body.get("tipoCrema"));
        if (body.containsKey("tipoPersonalizacion")) t.setTipoPersonalizacion((String) body.get("tipoPersonalizacion"));
        if (body.containsKey("precioPublico")) t.setPrecioPublico(toDouble(body.get("precioPublico")));
        if (body.containsKey("coste")) t.setCoste(toDouble(body.get("coste")));
        if (body.containsKey("disponible")) t.setDisponible((Boolean) body.get("disponible"));
        if (body.containsKey("notas")) t.setNotas((String) body.get("notas"));
    }

    private Double toDouble(Object val) {
        if (val instanceof Number) return ((Number) val).doubleValue();
        return 0.0;
    }
}
