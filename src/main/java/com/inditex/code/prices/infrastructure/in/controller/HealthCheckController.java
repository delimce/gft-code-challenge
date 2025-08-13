package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.domain.dto.health.HealthStatus;
import com.inditex.code.prices.domain.port.HealthCheckPort;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckPort healthCheckPort;

    public HealthCheckController(HealthCheckPort healthCheckPort) {
        this.healthCheckPort = healthCheckPort;
    }

    @GetMapping
    public ResponseEntity<HealthStatus> health() {
        HealthStatus status = healthCheckPort.health();

        if (status.status().equals(HealthStatus.DOWN)) {
            return ResponseEntity.status(503).body(status);
        }
        // If the status is UP, return 200 OK
        return ResponseEntity.ok(status);
    }
}
