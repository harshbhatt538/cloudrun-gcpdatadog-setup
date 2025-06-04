package com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Existing POST endpoint
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order) {
        // Fetch product
        Product product = restTemplate.getForObject(
            "http://product-service:8081/products/" + order.getProductId(),
            Product.class
        );
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Fetch user
        User user = restTemplate.getForObject(
            "http://user-service:8083/users/" + order.getUserId(),
            User.class
        );
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Save order
        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }

    // New GET endpoint to retrieve all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }
}
