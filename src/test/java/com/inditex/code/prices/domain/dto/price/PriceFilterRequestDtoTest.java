package com.inditex.code.prices.domain.dto.price;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriceFilterRequestDto Tests")
class PriceFilterRequestDtoTest {

    @Test
    @DisplayName("Should create PriceFilterRequestDto with all valid fields")
    void shouldCreatePriceFilterRequestDtoWithAllValidFields() {
        // Given
        LocalDateTime activeDate = LocalDateTime.of(2024, 6, 14, 10, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        // When
        PriceFilterRequestDto dto = new PriceFilterRequestDto(activeDate, productId, brandId);

        // Then
        assertEquals(activeDate, dto.activeDate());
        assertEquals(productId, dto.productId());
        assertEquals(brandId, dto.brandId());
    }

    @Test
    @DisplayName("Should create PriceFilterRequestDto with null values")
    void shouldCreatePriceFilterRequestDtoWithNullValues() {
        // When
        PriceFilterRequestDto dto = new PriceFilterRequestDto(null, null, null);

        // Then
        assertNull(dto.activeDate());
        assertNull(dto.productId());
        assertNull(dto.brandId());
    }

    @Test
    @DisplayName("Should implement equals correctly for same values")
    void shouldImplementEqualsCorrectlyForSameValues() {
        // Given
        LocalDateTime activeDate = LocalDateTime.of(2024, 6, 14, 10, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceFilterRequestDto dto1 = new PriceFilterRequestDto(activeDate, productId, brandId);
        PriceFilterRequestDto dto2 = new PriceFilterRequestDto(activeDate, productId, brandId);

        // Then
        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Should handle boundary date values")
    void shouldHandleBoundaryDateValues() {
        // Given
        LocalDateTime minDate = LocalDateTime.MIN;
        LocalDateTime maxDate = LocalDateTime.MAX;

        // When & Then
        assertDoesNotThrow(() -> {
            PriceFilterRequestDto dto1 = new PriceFilterRequestDto(minDate, 1L, 1L);
            PriceFilterRequestDto dto2 = new PriceFilterRequestDto(maxDate, 1L, 1L);

            assertEquals(minDate, dto1.activeDate());
            assertEquals(maxDate, dto2.activeDate());
        });
    }

}
