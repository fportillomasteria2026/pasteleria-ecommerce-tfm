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

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiUrl;

    public Map<String, Object> analyzeTartaImage(MultipartFile image) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

        String prompt = """
            Eres un experto en pasteleria. Analiza esta imagen de una tarta y devuelve un JSON con estos campos exactos:
            {
              "nombre": "nombre inventado para la tarta",
              "descripcion": "descripcion corta de lo que ves",
              "sabor": "sabor del bizcocho que ves",
              "crema": "tipo de crema que ves",
              "frutas": "frutas visibles separadas por coma",
              "forma": "Cilindrica o Cuadrada",
              "tamano": "XS, S, M, L o XL",
              "pisos": 2,
              "dimensiones": "dimensiones estimadas ej: h20xd25cm",
              "personalizacion": "Papel de Azucar, Papeleria o Mezcla",
              "hashtags": "hashtags separados por coma",
              "precio": 45
            }
            Analiza bien la imagen. Devuelve SOLO el JSON, sin texto adicional.
            """;

        Map<String, Object> requestBody = Map.of(
            "contents", java.util.List.of(Map.of(
                "parts", java.util.List.of(
                    Map.of("text", prompt),
                    Map.of("inline_data", Map.of(
                        "mime_type", mimeType,
                        "data", base64Image
                    ))
                )
            ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String url = geminiUrl + "?key=" + apiKey;
        log.info("Llamando a Gemini API: {}", url);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        log.info("Respuesta Gemini: {}", response.getBody());

        JsonNode root = objectMapper.readTree(response.getBody());
        String text = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
        String cleaned = text.trim().replaceAll("```json", "").replaceAll("```", "").trim();
        return objectMapper.readValue(cleaned, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
    }
}
