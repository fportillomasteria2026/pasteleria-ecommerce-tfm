package com.promptmaestro.controller;

import com.promptmaestro.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").trim();
        if (message.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("reply", "Escribe un mensaje por favor."));
        }
        String reply = chatService.chat(message);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, Object> request) {
        String tartaNombre = (String) request.getOrDefault("tartaNombre", "");
        String tamano = (String) request.getOrDefault("tamano", "M");
        String personalizacion = (String) request.getOrDefault("personalizacion", "");
        String notas = (String) request.getOrDefault("notas", "");
        String cliente = (String) request.getOrDefault("cliente", "Cliente");
        double precio = request.containsKey("precio") ? ((Number) request.get("precio")).doubleValue() : 0;

        String reply = chatService.buildOrderMessage(tartaNombre, tamano, personalizacion, notas, cliente, precio);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
