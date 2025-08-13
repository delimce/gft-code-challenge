package com.inditex.code.prices.domain.port;

import com.inditex.code.prices.domain.dto.health.HealthStatus;

/**
 * Input port for retrieving service health status.
 */
public interface HealthCheckPort {

    HealthStatus health();
}
