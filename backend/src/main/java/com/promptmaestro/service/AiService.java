package com.promptmaestro.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final VertexAiGeminiChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.vertex.ai.gemini.api-key}")
    private String apiKey;

    public AiService(VertexAiGeminiChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    public List<String> generateHashtags(MultipartFile image) throws IOException {
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
