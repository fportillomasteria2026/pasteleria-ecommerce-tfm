package com.promptmaestro.controller;

import com.promptmaestro.entity.Order;
import com.promptmaestro.entity.User;
import com.promptmaestro.repository.OrderRepository;
import com.promptmaestro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupController(UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
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

    @PostMapping("/seed-orders")
    public Map<String, Object> seedOrders() {
        long existing = orderRepository.count();
        if (existing > 0) {
            return Map.of("status", "skipped", "message", "Ya hay " + existing + " pedidos en la BD");
        }

        List<Order> samples = List.of(
            createOrder("Maria Garcia", "600 123 456", "Tarta de Chocolate", "L", "Papel de Azucar", "Decoracion con flores de chocolate", "COMPLETADO", 65.00),
            createOrder("Juan Lopez", "611 234 567", "Tarta Fresa Natural", "M", "Sin personalizacion", "Entregar antes de las 14h", "EN_PROCESO", 38.00),
            createOrder("Ana Martinez", "622 345 678", "Tarta Limon Merengada", "S", "Papeleria", "Con dedicatoria: Feliz CumpleañosLaura", "PENDIENTE", 42.00),
            createOrder("Pedro Sanchez", "633 456 789", "Tarta Red Velvet", "XL", "Papel de Azucar", "Para boda, 150 personas", "PENDIENTE", 95.00),
            createOrder("Laura Fernandez", "644 567 890", "Tarta Nuez y Caramelo", "M", "Sin personalizacion", "", "COMPLETADO", 78.00),
            createOrder("Carlos Ruiz", "655 678 901", "Tarta Vainilla Clasica", "L", "Mezcla", "Cumpleanos nina 5 anos, colores pastel", "EN_PROCESO", 52.00),
            createOrder("Elena Diaz", "666 789 012", "Tarta Chocolate", "S", "Sin personalizacion", "", "CANCELADO", 35.00),
            createOrder("Roberto Moreno", "677 890 123", "Tarta Fresa Natural", "XL", "Papel de Azucar", "Boda rural, servir 200 personas", "PENDIENTE", 120.00)
        );

        orderRepository.saveAll(samples);
        return Map.of("status", "created", "message", samples.size() + " pedidos de ejemplo creados", "count", samples.size());
    }

    private Order createOrder(String name, String phone, String tarta, String size, String personalization, String notes, String status, double total) {
        Order o = new Order();
        o.setCustomerName(name);
        o.setCustomerPhone(phone);
        o.setTartaName(tarta);
        o.setTartaSize(size);
        o.setPersonalization(personalization);
        o.setNotes(notes);
        o.setStatus(status);
        o.setTotalAmount(total);
        return o;
    }
}
