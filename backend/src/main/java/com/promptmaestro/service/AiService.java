package com.promptmaestro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiUrl;

    public AiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public List<String> generateHashtags(MultipartFile image) throws IOException {
        if (apiKey == null || apiKey.isEmpty()) {
            return List.of("#tarta", "#chocolate", "#pasteleria", "#local", "#testing");
        }

        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

        String prompt = """
            Analiza esta imagen de un producto de pasteleria/reposteria.
            Genera exactamente 5 hashtags descriptivos en español que describan visualmente el producto.
            Considera: tipo de producto, colores, ocasion, estilo, ingredientes visibles.
            Devuelve UNICAMENTE un array JSON con los 5 hashtags, sin texto adicional.
            Ejemplo de formato: ["#tarta","#chocolate","#boda","#fondant","#fresas"]
            """;

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(
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
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
            String cleaned = text.trim().replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readValue(cleaned, List.class);
        } catch (Exception e) {
            return List.of("#tarta", "#chocolate", "#pasteleria");
        }
    }

    public Map<String, Object> analyzeTartaImage(MultipartFile image) throws IOException {
        if (apiKey == null || apiKey.isEmpty()) {
            return getMockTartaAnalysis();
        }

        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

        String prompt = """
            Analiza esta imagen de una tarta de pasteleria. Devuelve un JSON con estos campos:
            {"nombre":"nombre sugerido","descripcion":"descripcion breve","sabor":"sabor del bizcocho","crema":"tipo de crema","frutas":"frutas visibles separadas por coma","forma":"Cilindrica o Cuadrada","tamano":"XS,S,M,L,XL","pisos":2,"dimensiones":"ej:h20xd25cm","personalizacion":"Papel de Azucar, Papeleria o Mezcla","hashtags":"chocolate,boda,fondant separados por coma","precio":35}
            Solo devuelve el JSON, sin texto adicional.
            """;

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(Map.of(
                "parts", List.of(
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

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
            String cleaned = text.trim().replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readValue(cleaned, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return getMockTartaAnalysis();
        }
    }

    private Map<String, Object> getMockTartaAnalysis() {
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
