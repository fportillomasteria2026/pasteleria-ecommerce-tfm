package com.promptmaestro.controller;

import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/admin/ai")
public class AiHashtagsController {

    @Autowired(required = false)
    private VertexAiGeminiChatModel chatModel;

    @Value("${spring.autoconfigure.exclude:}")
    private String excludeConfig;

    @PostMapping("/hashtags")
    public Map<String, String> generateHashtags(@RequestBody Map<String, String> request) {
        String nombre = request.getOrDefault("nombre", "");
        String descripcion = request.getOrDefault("descripcion", "");
        String sabor = request.getOrDefault("saborBizcocho", "");
        String crema = request.getOrDefault("tipoCrema", "");
        String frutas = request.getOrDefault("frutas", "");
        String forma = request.getOrDefault("forma", "");
        String tamano = request.getOrDefault("tamano", "");

        String prompt = String.format(
            "Eres un experto en reposteria y pasteleria. Genera entre 3 y 10 hashtags descriptivos para esta tarta. " +
            "Nombre: %s. Descripcion: %s. Bizcocho: %s. Crema: %s. Frutas: %s. Forma: %s. Tamano: %s. " +
            "Los hashtags deben ser cortos (1-2 palabras), en minusculas, sin #. Separalos con coma. " +
            "Ejemplo: chocolate, boda, fondant, fresa, cumpleanos",
            nombre, descripcion, sabor, crema, frutas, forma, tamano
        );

        if (chatModel == null || (excludeConfig != null && excludeConfig.contains("VertexAiGeminiAutoConfiguration"))) {
            return Map.of("hashtags", generateMockHashtags(nombre, sabor, crema, frutas));
        }

        try {
            String response = CompletableFuture.supplyAsync(() -> chatModel.call(prompt))
                    .get(15, TimeUnit.SECONDS);
            return Map.of("hashtags", response.trim());
        } catch (Exception e) {
            return Map.of("hashtags", generateMockHashtags(nombre, sabor, crema, frutas));
        }
    }

    private String generateMockHashtags(String nombre, String sabor, String crema, String frutas) {
        StringBuilder tags = new StringBuilder();
        if (sabor != null && !sabor.isEmpty()) tags.append(sabor.toLowerCase()).append(", ");
        if (crema != null && !crema.isEmpty()) tags.append(crema.toLowerCase()).append(", ");
        if (frutas != null && !frutas.isEmpty()) tags.append(frutas.toLowerCase().replace(", ", ",")).append(", ");
        tags.append("pasteleria, artesanal, dulce");
        return tags.toString();
    }
}
