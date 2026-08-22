package com.promptmaestro.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Autowired(required = false)
    private VertexAiGeminiChatModel chatModel;

    private final ObjectMapper objectMapper;

    @Value("${spring.autoconfigure.exclude:}")
    private String excludeConfig;

    public AiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<String> generateHashtags(MultipartFile image) throws IOException {
        if (chatModel == null || (excludeConfig != null && excludeConfig.contains("VertexAiGeminiAutoConfiguration"))) {
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

        String response = chatModel.call(prompt);
        return parseHashtags(response);
    }

    public Map<String, Object> analyzeTartaImage(MultipartFile image) throws IOException {
        if (chatModel == null || (excludeConfig != null && excludeConfig.contains("VertexAiGeminiAutoConfiguration"))) {
            return getMockTartaAnalysis();
        }

        String prompt = """
            Analiza esta imagen de una tarta de pasteleria. Devuelve un JSON con estos campos:
            {"nombre":"nombre sugerido","descripcion":"descripcion breve","sabor":"sabor del bizcocho","crema":"tipo de crema","frutas":"frutas visibles separadas por coma","forma":"Cilindrica o Cuadrada","tamano":"XS,S,M,L,XL","pisos":2,"dimensiones":"ej:h20xd25cm","personalizacion":"Papel de Azucar, Papeleria o Mezcla","hashtags":"chocolate,boda,fondant separados por coma","precio":35}
            Solo devuelve el JSON, sin texto adicional.
            """;

        try {
            String response = chatModel.call(prompt);
            return objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
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

    private List<String> parseHashtags(String aiResponse) {
        try {
            String cleaned = aiResponse.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();
            }
            return objectMapper.readValue(cleaned, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            try {
                Map<String, Object> map = objectMapper.readValue(
                        aiResponse.replaceAll("```json", "").replaceAll("```", "").trim(),
                        new TypeReference<Map<String, Object>>() {});
                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) map.getOrDefault("hashtags",
                        map.getOrDefault("tags", List.of()));
                return tags;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to parse AI response: " + aiResponse, ex);
            }
        }
    }
}
