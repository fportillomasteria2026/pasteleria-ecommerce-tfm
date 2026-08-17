package com.promptmaestro.controller;

import com.promptmaestro.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final UserRepository userRepository;

    public DebugController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Map<String, Object> getUserCount() {
        return Map.of(
            "count", userRepository.count(),
            "existsAdmin", userRepository.existsByUsername("admin")
        );
    }
}
