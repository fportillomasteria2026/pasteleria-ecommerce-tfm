package com.promptmaestro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
public class AiHashtagsController {

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/hashtags")
    public Map<String, String> generateHashtags(@RequestBody Map<String, String> request) {
        String nombre = request.getOrDefault("nombre", "");
        String sabor = request.getOrDefault("saborBizcocho", "");
        String crema = request.getOrDefault("tipoCrema", "");
        String frutas = request.getOrDefault("frutas", "");

        String prompt = String.format(
            "Genera entre 3 y 10 hashtags descriptivos para esta tarta. " +
            "Nombre: %s. Bizcocho: %s. Crema: %s. Frutas: %s. " +
            "Los hashtags deben ser cortos, en minusculas, sin #. Separalos con coma.",
            nombre, sabor, crema, frutas
        );

        if (apiKey == null || apiKey.isEmpty()) {
            return Map.of("hashtags", generateMockHashtags(nombre, sabor, crema, frutas));
        }

        try {
            Map<String, Object> requestBody = Map.of(
                "contents", java.util.List.of(Map.of("parts", java.util.List.of(Map.of("text", prompt))))
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiUrl + "?key=" + apiKey;
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response.getBody());
            String result = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText().trim();
            return Map.of("hashtags", result);
        } catch (Exception e) {
            return Map.of("hashtags", generateMockHashtags(nombre, sabor, crema, frutas));
        }
    }

    private String generateMockHashtags(String nombre, String sabor, String crema, String frutas) {
        StringBuilder tags = new StringBuilder();
        if (sabor != null && !sabor.isEmpty()) tags.append(sabor.toLowerCase()).append(", ");
        if (crema != null && !crema.isEmpty()) tags.append(crema.toLowerCase()).append(", ");
        if (frutas != null && !frutas.isEmpty()) tags.append(frutas.toLowerCase()).append(", ");
        tags.append("pasteleria, artesanal, dulce");
        return tags.toString();
    }
}
