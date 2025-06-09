package com.example.userservice;

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
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Value("${ENVIRONMENT:unknown}")
    private String environment;

    @GetMapping("/{id}")
    public ResponseWrapper<User> getUser(@PathVariable Long id) {
        logger.info("Fetching user with id {} in environment: {}", id, environment);

        // Add environment to Datadog trace
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            activeSpan.setTag("environment", environment);
        }

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return new ResponseWrapper<>(user, environment);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<User>> createUser(@RequestBody User user) {
        logger.info("Creating user with id {} in environment: {}", user.getId(), environment);

        // Add environment to Datadog trace
        Span activeSpan = GlobalTracer.get().activeSpan();
        if (activeSpan != null) {
            activeSpan.setTag("environment", environment);
        }

        User savedUser = userRepository.save(user);
        ResponseWrapper<User> response = new ResponseWrapper<>(savedUser, environment);
        return ResponseEntity.status(201).body(response);
    }
}

