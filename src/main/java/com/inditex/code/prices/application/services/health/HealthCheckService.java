package com.inditex.code.prices.application.services.health;

import com.inditex.code.prices.domain.dto.health.HealthStatus;
import com.inditex.code.prices.domain.port.HealthCheckPort;

import org.springframework.stereotype.Service;

/**
 * Application service implementing the health check use case.
 */
@Service
public class HealthCheckService implements HealthCheckPort {

    @Override
    public HealthStatus health() {

        try {
            // In a real case, verify dependencies (DB, messaging, etc.). For now, always
            // UP.
            return HealthStatus.up();
        } catch (Exception e) {
            // If any dependency check fails, return DOWN status
            return HealthStatus.down();
        }
    }
}
