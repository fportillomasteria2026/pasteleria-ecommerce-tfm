package com.promptmaestro.controller;

import com.promptmaestro.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
public class AiTartaController {

    private final AiService aiService;

    public AiTartaController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyze-tarta")
    public ResponseEntity<Map<String, Object>> analyzeTarta(@RequestParam("image") MultipartFile image) throws IOException {
        Map<String, Object> result = aiService.analyzeTartaImage(image);
        return ResponseEntity.ok(result);
    }
}
