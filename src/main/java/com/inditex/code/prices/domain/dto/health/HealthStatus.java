package com.inditex.code.prices.domain.dto.health;

/**
 * Domain model representing a simple health status.
 */
public record HealthStatus(String status) {
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";

    public static HealthStatus up() {
        return new HealthStatus(UP);
    }

    public static HealthStatus down() {
        return new HealthStatus(DOWN);
    }
}
