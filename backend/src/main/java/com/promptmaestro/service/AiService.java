package com.promptmaestro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent}")
    private String geminiUrl;

    public Map<String, Object> analyzeTartaImage(MultipartFile image) throws IOException {
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("GEMINI_API_KEY no configurada, usando mock");
            return getMockTartaAnalysis();
        }

        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

        String prompt = "Eres un experto en pasteleria. Analiza esta imagen de una tarta y devuelve un JSON con estos campos: nombre, descripcion, sabor, crema, frutas, forma, tamano, pisos, dimensiones, personalizacion, hashtags, precio. Devuelve SOLO el JSON.";

        Map<String, Object> requestBody = Map.of(
            "contents", java.util.List.of(Map.of(
                "parts", java.util.List.of(
                    Map.of("text", prompt),
                    Map.of("inline_data", Map.of("mime_type", mimeType, "data", base64Image))
                ))
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        String url = geminiUrl + "?key=" + apiKey;

        log.info("Llamando a Gemini API...");
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        log.info("Gemini HTTP status: {}", response.getStatusCode());

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw new RuntimeException("Gemini HTTP " + response.getStatusCodeValue() + ": " + response.getBody());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        if (root.has("error")) {
            throw new RuntimeException("Gemini: " + root.get("error").get("message").asText());
        }

        String text = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
        String cleaned = text.trim().replaceAll("```json", "").replaceAll("```", "").trim();
        return objectMapper.readValue(cleaned, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
    }

    public Map<String, Object> getMockTartaAnalysis() {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("nombre", "Tarta Artesanal");
        result.put("descripcion", "Tarta elaborada con ingredientes de primera calidad");
        result.put("sabor", "Chocolate");
        result.put("crema", "Ganache");
        result.put("frutas", "Fresa");
        result.put("forma", "Cilindrica");
        result.put("tamano", "M");
        result.put("pisos", 2);
        result.put("dimensiones", "h20xd25cm");
        result.put("personalizacion", "Papel de Azucar");
        result.put("hashtags", "chocolate,fresa,ganache,pasteleria,artesanal");
        result.put("precio", 45);
        return result;
    }
}
