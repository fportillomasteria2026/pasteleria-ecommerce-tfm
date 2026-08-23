package com.promptmaestro.controller;

import com.promptmaestro.entity.Order;
import com.promptmaestro.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(orderRepository.findByStatus(status));
        }
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(orderRepository.findByCustomerNameContainingIgnoreCase(search));
        }
        return ResponseEntity.ok(orderRepository.findAllByOrderByCreatedAtDesc());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                              @Valid @RequestBody Order updated) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        order.setCustomerName(updated.getCustomerName());
        order.setCustomerPhone(updated.getCustomerPhone());
        order.setTartaName(updated.getTartaName());
        order.setTartaSize(updated.getTartaSize());
        order.setPersonalization(updated.getPersonalization());
        order.setNotes(updated.getNotes());
        order.setStatus(updated.getStatus());
        order.setTotalAmount(updated.getTotalAmount());
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
                                               @RequestBody Map<String, String> body) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        order.setStatus(body.getOrDefault("status", order.getStatus()));
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
