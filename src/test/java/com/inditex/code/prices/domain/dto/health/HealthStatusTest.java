package com.inditex.code.prices.domain.dto.health;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HealthStatus DTO Tests")
class HealthStatusTest {

    @Test
    @DisplayName("Should create HealthStatus with provided status")
    void shouldCreateHealthStatusWithProvidedStatus() {
        // Given
        String status = "HEALTHY";

        // When
        HealthStatus healthStatus = new HealthStatus(status);

        // Then
        assertEquals(status, healthStatus.status());
    }

    @Test
    @DisplayName("Should create UP status using factory method")
    void shouldCreateUpStatusUsingFactoryMethod() {
        // When
        HealthStatus healthStatus = HealthStatus.up();

        // Then
        assertEquals(HealthStatus.UP, healthStatus.status());
        assertEquals("UP", healthStatus.status());
    }

    @Test
    @DisplayName("Should create DOWN status using factory method")
    void shouldCreateDownStatusUsingFactoryMethod() {
        // When
        HealthStatus healthStatus = HealthStatus.down();

        // Then
        assertEquals(HealthStatus.DOWN, healthStatus.status());
        assertEquals("DOWN", healthStatus.status());
    }

    @Test
    @DisplayName("Should handle null status")
    void shouldHandleNullStatus() {
        // When
        HealthStatus healthStatus = new HealthStatus(null);

        // Then
        assertNull(healthStatus.status());
    }
}
