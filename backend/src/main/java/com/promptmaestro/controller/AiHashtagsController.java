package com.promptmaestro.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
public class AiHashtagsController {

    private static final Logger log = LoggerFactory.getLogger(AiHashtagsController.class);

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent}")
    private String geminiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/hashtags")
    public Map<String, String> generateHashtags(@RequestBody Map<String, String> request) {
        String nombre = request.getOrDefault("nombre", "");
        String sabor = request.getOrDefault("saborBizcocho", "");
        String crema = request.getOrDefault("tipoCrema", "");
        String frutas = request.getOrDefault("frutas", "");

        String prompt = String.format(
            "Eres un experto en marketing digital y reposteria. " +
            "Analiza esta tarta: Nombre=%s, Bizcocho=%s, Crema=%s, Frutas=%s. " +
            "DEVUELVE EXACTAMENTE 15 hashtags separados por coma. " +
            "Cada hashtag DEBE empezar con # y estar en minusculas. " +
            "Ninguno puede repetirse. " +
            "Incluye: tipo de producto, ocasion, colores, ingredientes, estilo, decoracion, estacion, textura, forma. " +
            "Ejemplo de respuesta: #chocolate,#tarta,#boda,#fondant,#fresa,#cumpleanos,#decoracion,#dulce,#crema,#hojaldre,#reposteria,#elegante,#personalizado,#bonito,#exquisito " +
            "DEVUELVE SOLO LA LISTA DE 15 HASHTAGS SEPARADOS POR COMA, NADA MAS.",
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
            log.info("Llamando a Gemini para hashtags");
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String result = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText().trim();
            // Deduplicar y asegurar formato #hashtag
            String[] tags = result.split(",");
            java.util.LinkedHashSet<String> unique = new java.util.LinkedHashSet<>();
            for (String tag : tags) {
                String t = tag.trim().toLowerCase();
                if (!t.startsWith("#")) t = "#" + t;
                unique.add(t);
            }
            String finalResult = String.join(", ", unique);
            log.info("Gemini hashtags ({}): {}", unique.size(), finalResult);
            return Map.of("hashtags", finalResult);
        } catch (Exception e) {
            log.error("Error Gemini hashtags: {}", e.getMessage());
            return Map.of("hashtags", generateMockHashtags(nombre, sabor, crema, frutas));
        }
    }

    private String generateMockHashtags(String nombre, String sabor, String crema, String frutas) {
        StringBuilder tags = new StringBuilder("#pasteleria, #artesanal, #dulce");
        if (sabor != null && !sabor.isEmpty()) tags.insert(0, "#" + sabor.toLowerCase() + ", ");
        if (crema != null && !crema.isEmpty()) tags.insert(0, "#" + crema.toLowerCase() + ", ");
        if (frutas != null && !frutas.isEmpty()) tags.append(", #").append(frutas.toLowerCase().replace(", ", ", #"));
        return tags.toString();
    }
}
