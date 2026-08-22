package com.promptmaestro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @Autowired(required = false)
    private VertexAiGeminiChatModel chatModel;

    @Value("${spring.autoconfigure.exclude:}")
    private String excludeConfig;

    // Cache de respuestas frecuentes (ultimas 20 preguntas)
    private final LinkedHashMap<String, String> responseCache = new LinkedHashMap<>(20, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > 20;
        }
    };

    private static final String BUSINESS_CONTEXT = """
        Eres "Dulce Sabor", asistente virtual de una pasteleria artesanal en Sevilla, Malaga.

        DATOS DEL NEGOCIO:
        - Nombre: Dulce Sabor Pasteleria Artesanal
        - Direccion: C/ del Chocolate, 15, CP 41001 Sevilla, Malaga
        - Telefono: 955 123 456
        - Horario: Lunes a Sabado, 9:00 - 20:00
        - WhatsApp: 955 123 456

        PRODUCTOS:
        - Tartas personalizadas (bodas, cumpleanos, comuniones)
        - Pasteleria fina (croissants, macarons, napolitanas)
        - Postres (tiramisu, cheesecake, vasos de bizcocho)
        - Galletas y cookies artesanales
        - Bandejas surtidas y cajas de regalo

        REGLAS ESTRICTAS:
        1. Responde SOLO en espanol, maximo 3 lineas
        2. Usa tono cercano y profesional
        3. NUNCA inventes precios - di "Llamanos al 955 123 456 para presupuesto"
        4. Para pedidos, recomienda WhatsApp: 955 123 456
        5. Si la pregunta no es del negocio, di: "Solo puedo ayudarte con consultas de la pasteleria"
        6. No hagas preguntas al usuario, solo responde
        """;

    public String chat(String userMessage) {
        String normalizedMsg = messageKey(userMessage);

        // 1. Buscar en cache
        String cached = responseCache.get(normalizedMsg);
        if (cached != null) {
            log.info("Cache hit para: {}", normalizedMsg);
            return cached;
        }

        // 2. Si Gemini no esta disponible, usar mock
        if (chatModel == null || (excludeConfig != null && excludeConfig.contains("VertexAiGeminiAutoConfiguration"))) {
            String mockResponse = getMockResponse(userMessage);
            responseCache.put(normalizedMsg, mockResponse);
            return mockResponse;
        }

        // 3. Llamar a Gemini con timeout
        try {
            String prompt = BUSINESS_CONTEXT + "\n\nPregunta del cliente: " + userMessage;
            String response = CompletableFuture.supplyAsync(() -> chatModel.call(prompt))
                    .get(15, TimeUnit.SECONDS);

            // Limpiar respuesta
            response = cleanResponse(response);
            responseCache.put(normalizedMsg, response);
            return response;

        } catch (Exception e) {
            log.error("Error llamando a Gemini: {}", e.getMessage());
            String fallback = getMockResponse(userMessage);
            responseCache.put(normalizedMsg, fallback);
            return fallback;
        }
    }

    private String messageKey(String msg) {
        return msg.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    private String cleanResponse(String response) {
        if (response == null) return "Disculpa, no pude procesar tu pregunta.";
        String cleaned = response.trim();
        // Si la respuesta es muy larga, cortar a 3 lineas
        String[] lines = cleaned.split("\n");
        if (lines.length > 3) {
            cleaned = lines[0] + "\n" + lines[1] + "\n" + lines[2];
        }
        return cleaned;
    }

    private String getMockResponse(String message) {
        String lower = message.toLowerCase();

        if (lower.contains("hola") || lower.contains("buenos") || lower.contains("buenas")) {
            return "Hola! Bienvenido a Dulce Sabor. En que puedo ayudarte?";
        }
        if (lower.contains("gracias")) {
            return "De nada! Si necesitas algo mas, aqui estamos.";
        }
        if (lower.contains("horario") || lower.contains("hora") || lower.contains("abierto")) {
            return "Nuestro horario es de Lunes a Sabado de 9:00 a 20:00. Te esperamos!";
        }
        if (lower.contains("direccion") || lower.contains("donde") || lower.contains("ubicacion")) {
            return "Estamos en C/ del Chocolate, 15, 41001 Sevilla, Malaga.";
        }
        if (lower.contains("telefono") || lower.contains("llamar")) {
            return "Puedes llamarnos al 955 123 456.";
        }
        if (lower.contains("whatsapp")) {
            return "Escribenos por WhatsApp al 955 123 456 y te atendemos!";
        }
        if (lower.contains("tarta") || lower.contains("pastel") || lower.contains("cumplea")) {
            return "Hacemos tartas personalizadas para toda ocasion. Llamanos para presupuesto sin compromiso!";
        }
        if (lower.contains("precio") || lower.contains("coste") || lower.contains("cuanto")) {
            return "Cada producto es unico! Llamanos al 955 123 456 para presupuesto personalizado.";
        }
        if (lower.contains("pedido") || lower.contains("pedir") || lower.contains("encargar")) {
            return "Haz tu pedido por WhatsApp al 955 123 456 o llamanos directamente.";
        }
        if (lower.contains("galleta") || lower.contains("cookie")) {
            return "Tenemos galletas artesanales de mantequilla, chocolate y frutas. Pregunta por nuestras cookies especiales!";
        }
        if (lower.contains("regalo") || lower.contains("caja")) {
            return "Tenemos cajas de regalo y bandejas surtidas perfectas para cualquier occasion. Llamanos para informacion!";
        }

        return "Para esa consulta, llamame al 955 123 456 o escribeme por WhatsApp. En que mas puedo ayudarte?";
    }
}
