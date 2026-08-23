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

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
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

    @PostMapping("/generate-description")
    public Map<String, String> generateDescription(@RequestBody Map<String, String> request) {
        String nombre = request.getOrDefault("nombre", "");
        String sabor = request.getOrDefault("saborBizcocho", "");
        String crema = request.getOrDefault("tipoCrema", "");
        String frutas = request.getOrDefault("frutas", "");
        String forma = request.getOrDefault("forma", "");
        String tamano = request.getOrDefault("tamano", "");
        String pisos = request.getOrDefault("pisos", "2");
        String personalizacion = request.getOrDefault("tipoPersonalizacion", "");

        String prompt = String.format(
            "Eres un copywriter experto en pasteleria artesanal. " +
            "Genera una descripcion de marketing para esta tarta:\n" +
            "Nombre: %s\nBizcocho: %s\nCrema: %s\nFrutas: %s\nForma: %s\nTamano: %s\nPisos: %s\nPersonalizacion: %s\n\n" +
            "REGLAS:\n" +
            "- Maximo 2-3 lineas, tono elegante y apetitoso\n" +
            "- Destaca los ingredientes y la artesania\n" +
            "- NO incluyas el precio\n" +
            "- Devuelve SOLO la descripcion, sin comillas ni formato adicional",
            nombre, sabor, crema, frutas, forma, tamano, pisos, personalizacion
        );

        if (apiKey == null || apiKey.isEmpty()) {
            return Map.of("descripcion", generateMockDescription(nombre, sabor, crema, frutas));
        }

        try {
            Map<String, Object> requestBody = Map.of(
                "contents", java.util.List.of(Map.of("parts", java.util.List.of(Map.of("text", prompt))))
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiUrl + "?key=" + apiKey;
            log.info("Llamando a Gemini para descripcion");
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String result = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText().trim();
            log.info("Gemini descripcion: {}", result.substring(0, Math.min(result.length(), 80)));
            return Map.of("descripcion", result);
        } catch (Exception e) {
            log.warn("Gemini no disponible ({}), usando mock descripcion", e.getMessage());
            return Map.of("descripcion", generateMockDescription(nombre, sabor, crema, frutas));
        }
    }

    @PostMapping("/inventory-optimize")
    public Map<String, Object> optimizeInventory(@RequestBody java.util.List<Map<String, Object>> items) {
        StringBuilder inventoryList = new StringBuilder();
        for (Map<String, Object> item : items) {
            inventoryList.append(String.format(
                "- %s (Marca: %s, Proveedor: %s): Stock=%.2f %s, Coste=%.2f EUR/%s%n",
                item.getOrDefault("nombre", ""),
                item.getOrDefault("marca", ""),
                item.getOrDefault("proveedor", ""),
                item.getOrDefault("cantidad", 0),
                item.getOrDefault("unidad", ""),
                item.getOrDefault("coste", 0),
                item.getOrDefault("formato", "")
            ));
        }

        String prompt = String.format(
            "Eres un experto en gestion de inventario para pasteleria artesanal. " +
            "Analiza el inventario actual y sugiere acciones de reposicion.\n\n" +
            "INVENTARIO ACTUAL:\n%s\n\n" +
            "Devuelve un JSON con un array 'sugerencias' donde cada elemento tiene:\n" +
            "- \"nombre\": nombre del producto\n" +
            "- \"proveedor\": proveedor\n" +
            "- \"stockActual\": cantidad actual\n" +
            "- \"unidad\": unidad de medida\n" +
            "- \"cantidadSugerida\": cuantos unidades reponer\n" +
            "- \"costeEstimado\": coste estimado de reposicion\n" +
            "- \"prioridad\": \"alta\", \"media\" o \"baja\"\n" +
            "- \"motivo\": razon breve de la sugerencia\n\n" +
            "CRITERIOS:\n" +
            "- Prioridad ALTA: stock < 5 unidades o ingredientes criticos\n" +
            "- Prioridad MEDIA: stock entre 5-15 unidades\n" +
            "- Prioridad BAJA: stock > 15 unidades pero se recomienda mantener\n" +
            "DEVUELVE SOLO EL JSON, sin texto adicional.",
            inventoryList.toString()
        );

        if (apiKey == null || apiKey.isEmpty()) {
            return getMockInventoryAnalysis(items);
        }

        try {
            Map<String, Object> requestBody = Map.of(
                "contents", java.util.List.of(Map.of("parts", java.util.List.of(Map.of("text", prompt))))
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiUrl + "?key=" + apiKey;
            log.info("Llamando a Gemini para optimizacion de inventario");
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            String text = root.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText().trim();
            String cleaned = text.replaceAll("```json", "").replaceAll("```", "").trim();
            Map<String, Object> result = objectMapper.readValue(cleaned, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
            log.info("Gemini inventario: {} sugerencias", ((java.util.List<?>) result.getOrDefault("sugerencias", java.util.List.of())).size());
            return result;
        } catch (Exception e) {
            log.warn("Gemini no disponible ({}), usando mock inventario", e.getMessage());
            return getMockInventoryAnalysis(items);
        }
    }

    private String generateMockDescription(String nombre, String sabor, String crema, String frutas) {
        StringBuilder desc = new StringBuilder();
        desc.append(nombre != null && !nombre.isEmpty() ? nombre : "Tarta artesanal");
        if (sabor != null && !sabor.isEmpty()) desc.append(" de ").append(sabor.toLowerCase());
        if (crema != null && !crema.isEmpty()) desc.append(" con ").append(crema.toLowerCase());
        if (frutas != null && !frutas.isEmpty()) desc.append(" y ").append(frutas.toLowerCase());
        desc.append(". Elaborada con ingredientes de primera calidad en nuestra pasteleria artesanal de Malaga.");
        return desc.toString();
    }

    private Map<String, Object> getMockInventoryAnalysis(java.util.List<Map<String, Object>> items) {
        java.util.List<Map<String, Object>> sugerencias = new java.util.ArrayList<>();
        for (Map<String, Object> item : items) {
            double cantidad = ((Number) item.getOrDefault("cantidad", 0)).doubleValue();
            double coste = ((Number) item.getOrDefault("coste", 0)).doubleValue();
            String prioridad;
            double cantidadSugerida;
            String motivo;

            if (cantidad < 5) {
                prioridad = "alta";
                cantidadSugerida = 20;
                motivo = "Stock critico, reposicion urgente";
            } else if (cantidad < 15) {
                prioridad = "media";
                cantidadSugerida = 15;
                motivo = "Stock bajo, recomendar reposicion";
            } else {
                prioridad = "baja";
                cantidadSugerida = 10;
                motivo = "Stock suficiente, mantener nivel";
            }

            Map<String, Object> sugerencia = new java.util.HashMap<>();
            sugerencia.put("nombre", item.getOrDefault("nombre", ""));
            sugerencia.put("proveedor", item.getOrDefault("proveedor", ""));
            sugerencia.put("stockActual", cantidad);
            sugerencia.put("unidad", item.getOrDefault("unidad", ""));
            sugerencia.put("cantidadSugerida", cantidadSugerida);
            sugerencia.put("costeEstimado", coste * cantidadSugerida);
            sugerencia.put("prioridad", prioridad);
            sugerencia.put("motivo", motivo);
            sugerencias.add(sugerencia);
        }
        return Map.of("sugerencias", sugerencias);
    }

    private String generateMockHashtags(String nombre, String sabor, String crema, String frutas) {
        StringBuilder tags = new StringBuilder("#pasteleria, #artesanal, #dulce");
        if (sabor != null && !sabor.isEmpty()) tags.insert(0, "#" + sabor.toLowerCase() + ", ");
        if (crema != null && !crema.isEmpty()) tags.insert(0, "#" + crema.toLowerCase() + ", ");
        if (frutas != null && !frutas.isEmpty()) tags.append(", #").append(frutas.toLowerCase().replace(", ", ", #"));
        return tags.toString();
    }
}
