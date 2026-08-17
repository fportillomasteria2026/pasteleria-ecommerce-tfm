package com.promptmaestro.controller;

import com.promptmaestro.entity.User;
import com.promptmaestro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/init-admin")
    public Map<String, String> initAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role("ADMIN")
                    .build();
            userRepository.save(admin);
            return Map.of("status", "created", "message", "Admin user created");
        }
        return Map.of("status", "exists", "message", "Admin user already exists");
    }

    @GetMapping("/check")
    public Map<String, Object> check() {
        return Map.of(
            "userCount", userRepository.count(),
            "adminExists", userRepository.existsByUsername("admin")
        );
    }
}
