package com.promptmaestro.controller;

import com.promptmaestro.entity.Order;
import com.promptmaestro.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                              @Valid @RequestBody Order updated) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setCustomerName(updated.getCustomerName());
        order.setStatus(updated.getStatus());
        order.setTotalAmount(updated.getTotalAmount());
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
