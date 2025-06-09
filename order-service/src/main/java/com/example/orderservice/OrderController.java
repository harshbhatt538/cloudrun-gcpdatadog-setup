package com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ENVIRONMENT:unknown}")
    private String environment;

    @Value("${PRODUCT_SERVICE_URL:http://product-service:8081}")
    private String productServiceUrl;

    @Value("${USER_SERVICE_URL:http://user-service:8083}")
    private String userServiceUrl;

    @PostMapping
    public ResponseEntity<ResponseWrapper<Void>> createOrder(@RequestBody Order order) {
        logger.info("Creating order with id {} in environment: {}", order.getId(), environment);

        // Add environment to Datadog trace
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            activeSpan.setTag("environment", environment);
        }

        // Fetch product
        Product product = restTemplate.getForObject(
            productServiceUrl + "/products/" + order.getProductId(),
            Product.class
        );
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Fetch user
        User user = restTemplate.getForObject(
            userServiceUrl + "/users/" + order.getUserId(),
            User.class
        );
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Save order
        orderRepository.save(order);
        ResponseWrapper<Void> response = new ResponseWrapper<>(null, environment);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<Order>>> getAllOrders() {
        logger.info("Fetching all orders in environment: {}", environment);

        // Add environment to Datadog trace
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            activeSpan.setTag("environment", environment);
        }

        List<Order> orders = orderRepository.findAll();
        ResponseWrapper<List<Order>> response = new ResponseWrapper<>(orders, environment);
        return ResponseEntity.ok(response);
    }
}
