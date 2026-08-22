package com.promptmaestro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final LinkedHashMap<String, String> responseCache = new LinkedHashMap<>(20, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > 20;
        }
    };

    private static final String BUSINESS_CONTEXT = """
        Eres "Belieta", asistente virtual de una pasteleria artesanal en Sevilla.
        Responde SOLO sobre el negocio. Maximo 3 lineas.
        Si no sabes, di: "Para esa consulta, llamame al 955 123 456"
        NUNCA inventes precios. Di que son personalizados.
        """;

    public String chat(String userMessage) {
        String normalizedMsg = userMessage.toLowerCase().trim().replaceAll("\\s+", " ");
        String cached = responseCache.get(normalizedMsg);
        if (cached != null) return cached;

        if (apiKey == null || apiKey.isEmpty()) {
            String mock = getMockResponse(userMessage);
            responseCache.put(normalizedMsg, mock);
            return mock;
        }

        try {
            String prompt = BUSINESS_CONTEXT + "\n\nPregunta del cliente: " + userMessage;
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiUrl + "?key=" + apiKey;
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String result = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText().trim();
            responseCache.put(normalizedMsg, result);
            return result;
        } catch (Exception e) {
            log.error("Error Gemini: {}", e.getMessage());
            String fallback = getMockResponse(userMessage);
            responseCache.put(normalizedMsg, fallback);
            return fallback;
        }
    }

    private String getMockResponse(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("hola") || lower.contains("buenos")) return "Hola! Bienvenido a Belieta. En que puedo ayudarte?";
        if (lower.contains("gracias")) return "De nada! Si necesitas algo mas, aqui estamos.";
        if (lower.contains("horario") || lower.contains("hora")) return "Nuestro horario es Lunes a Sabado de 9:00 a 20:00.";
        if (lower.contains("direccion") || lower.contains("donde")) return "Estamos en C/ del Chocolate, 15, Sevilla.";
        if (lower.contains("telefono") || lower.contains("llamar")) return "Puedes llamarnos al 955 123 456.";
        if (lower.contains("whatsapp")) return "Escribenos por WhatsApp al 955 123 456.";
        if (lower.contains("tarta") || lower.contains("pastel")) return "Hacemos tartas personalizadas para toda ocasion. Llamanos para presupuesto!";
        if (lower.contains("precio") || lower.contains("cuanto")) return "Cada producto es unico! Llamanos al 955 123 456 para presupuesto personalizado.";
        if (lower.contains("pedido") || lower.contains("pedir")) return "Haz tu pedido por WhatsApp al 955 123 456 o llamanos directamente.";
        return "Para esa consulta, llamame al 955 123 456 o escribeme por WhatsApp. En que mas puedo ayudarte?";
    }
}
