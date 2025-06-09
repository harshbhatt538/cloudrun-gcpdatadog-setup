package com.example.productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @Value("${ENVIRONMENT:unknown}")
    private String environment;

    @GetMapping("/{id}")
    public ResponseWrapper<Product> getProduct(@PathVariable Long id) {
        logger.info("Fetching product with id {} in environment: {}", id, environment);

        // Add environment to Datadog trace
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            activeSpan.setTag("environment", environment);
        }

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ResponseWrapper<>(product, environment);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Product>> createProduct(@RequestBody Product product) {
        logger.info("Creating product with id {} in environment: {}", product.getId(), environment);

        // Add environment to Datadog trace
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            activeSpan.setTag("environment", environment);
        }

        Product savedProduct = productRepository.save(product);
        ResponseWrapper<Product> response = new ResponseWrapper<>(savedProduct, environment);
        return ResponseEntity.status(201).body(response);
    }
}
