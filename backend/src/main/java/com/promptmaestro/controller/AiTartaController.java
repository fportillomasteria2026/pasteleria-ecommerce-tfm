package com.promptmaestro.controller;

import com.promptmaestro.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
public class AiTartaController {

    private static final Logger log = LoggerFactory.getLogger(AiTartaController.class);
    private final AiService aiService;

    public AiTartaController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyze-tarta")
    public ResponseEntity<?> analyzeTarta(@RequestParam("image") MultipartFile image) {
        try {
            Map<String, Object> result = aiService.analyzeTartaImage(image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error analizando tarta: {}", e.getMessage(), e);
            // Devolver mock con el error
            Map<String, Object> mockResult = aiService.getMockTartaAnalysis();
            mockResult.put("_error", "IA no disponible: " + e.getMessage() + ". Usando datos de ejemplo.");
            return ResponseEntity.ok(mockResult);
        }
    }
}
