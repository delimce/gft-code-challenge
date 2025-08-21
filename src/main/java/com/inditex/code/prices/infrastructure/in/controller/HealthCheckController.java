package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.api.HealthApi;
import com.inditex.code.prices.domain.dto.health.HealthStatus;
import com.inditex.code.prices.domain.port.HealthCheckPort;
import com.inditex.code.prices.domain.mapper.HealthMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthApi {

    private final HealthCheckPort healthCheckPort;
    private final HealthMapper healthMapper;

    public HealthCheckController(HealthCheckPort healthCheckPort, HealthMapper healthMapper) {
        this.healthCheckPort = healthCheckPort;
        this.healthMapper = healthMapper;
    }

    @Override
    public ResponseEntity<com.inditex.code.prices.api.model.HealthStatus> _getHealth() {
        HealthStatus status = healthCheckPort.health();
        com.inditex.code.prices.api.model.HealthStatus apiStatus = healthMapper.toApiModel(status);

        if (status.status().equals(HealthStatus.DOWN)) {
            return ResponseEntity.status(503).body(apiStatus);
        }
        // If the status is UP, return 200 OK
        return ResponseEntity.ok(apiStatus);
    }
}
