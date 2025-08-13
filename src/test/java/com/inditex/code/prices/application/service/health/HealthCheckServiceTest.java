package com.inditex.code.prices.application.service.health;

import com.inditex.code.prices.application.services.health.HealthCheckService;
import com.inditex.code.prices.domain.dto.health.HealthStatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthCheckServiceTest {

    private final HealthCheckService service = new HealthCheckService();

    @Test
    void health_shouldReturnUp() {
        HealthStatus status = service.health();
        assertEquals(HealthStatus.UP, status.status());
    }
}
