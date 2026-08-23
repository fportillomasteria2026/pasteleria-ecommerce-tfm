package com.promptmaestro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptmaestro.entity.Tarta;
import com.promptmaestro.repository.TartaRepository;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TartaRepository tartaRepository;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
    private String geminiUrl;

    public ChatService(TartaRepository tartaRepository) {
        this.tartaRepository = tartaRepository;
    }

    public String chat(String userMessage) {
        log.info("Chat recibido: {}", userMessage);
        if (apiKey == null || apiKey.isEmpty()) {
            log.info("Gemini API key no configurada, usando mock");
            return getMockResponse(userMessage);
        }

        List<Tarta> tartas = tartaRepository.findByActivoTrue();
        StringBuilder productosInfo = new StringBuilder();
        for (Tarta t : tartas) {
            productosInfo.append(String.format(
                "- %s: %s, %s, %s, %s, %s pisos, %s EUR%n",
                t.getNombre(), t.getDescripcion() != null ? t.getDescripcion() : "",
                t.getSaborBizcocho() != null ? t.getSaborBizcocho() : "",
                t.getTipoCrema() != null ? t.getTipoCrema() : "",
                t.getFrutas() != null ? t.getFrutas() : "",
                t.getPisos(),
                t.getPrecioPublico()
            ));
        }

        String context = String.format("""
            Eres el asistente virtual de DULCE SABOR, pastelería artesanal en Málaga.
            Dirección: C/ Marqués de Larios, 1, 29005 Málaga
            Teléfono: 955 123 456
            Horario: Lunes a Sábado, 9:00 - 20:00
            WhatsApp: 955 123 456

            PRODUCTOS ACTUALES EN LA TIENDA:
            %s

            REGLAS:
            - Responde en español, breve y amable (máximo 3-4 líneas)
            - Si preguntan por precios, da el precio del producto si está en la lista
            - Si preguntan por un producto, busca en la lista y responde con sus detalles
            - Para pedidos, recomienda WhatsApp: 955 123 456
            - Si no sabes algo, di: "Para esa consulta, llámame al 955 123 456"
            """, productosInfo.toString());

        String prompt = context + "\nPregunta del cliente: " + userMessage;

        try {
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiUrl + "?key=" + apiKey;

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String reply = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText().trim();
            log.info("Gemini reply: {}", reply.substring(0, Math.min(reply.length(), 100)));
            return reply;

        } catch (Exception e) {
            log.warn("Gemini no disponible ({}), usando mock para: {}", e.getMessage(), userMessage);
            return getMockResponse(userMessage);
        }
    }

    private String getMockResponse(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("hola") || lower.contains("buenos")) return "¡Hola! Bienvenido a Dulce Sabor. ¿En qué puedo ayudarte?";
        if (lower.contains("gracias")) return "¡De nada! Si necesitas algo más, aquí estamos.";
        if (lower.contains("horario") || lower.contains("hora")) return "Nuestro horario es Lunes a Sábado de 9:00 a 20:00.";
        if (lower.contains("direccion") || lower.contains("donde")) return "Estamos en C/ Marqués de Larios, 1, 29005 Málaga.";
        if (lower.contains("telefono") || lower.contains("llamar")) return "Puedes llamarnos al 955 123 456.";
        if (lower.contains("whatsapp")) return "Escríbenos por WhatsApp al 955 123 456.";
        if (lower.contains("tarta") || lower.contains("pastel")) return "Hacemos tartas personalizadas para toda ocasión. ¡Llámanos para presupuesto!";
        if (lower.contains("precio") || lower.contains("cuanto")) return "¡Llámanos al 955 123 456 para presupuesto personalizado!";
        if (lower.contains("pedido") || lower.contains("pedir")) return "Haz tu pedido por WhatsApp al 955 123 456.";
        return "Para esa consulta, llámame al 955 123 456 o escríbeme por WhatsApp. ¿En qué más puedo ayudarte?";
    }

    public String buildOrderMessage(String tartaNombre, String tamano, String personalizacion, String notas, String cliente, double precio) {
        String msg = String.format(
            "*Pedido - Dulce Sabor*\n\n" +
            "Cliente: %s\n" +
            "Tarta: %s\n" +
            "Tamaño: %s\n" +
            "Personalización: %s\n" +
            (notas != null && !notas.isEmpty() ? "Notas: %s\n" : "") +
            (precio > 0 ? "Precio: %.2f EUR\n" : "") +
            "\nDirección: C/ Marqués de Larios, 1, 29005 Málaga\n" +
            "Horario: Lun-Sáb 9:00-20:00\n\n" +
            "Para confirmar el pedido, envía este mensaje por WhatsApp al 955 123 456",
            cliente, tartaNombre, tamano,
            personalizacion != null && !personalizacion.isEmpty() ? personalizacion : "Sin personalización",
            notas, precio
        );
        log.info("Pedido generado para {} - {} {}", cliente, tartaNombre, tamano);
        return msg;
    }
}
