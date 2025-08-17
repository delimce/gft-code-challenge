package com.inditex.code.prices.domain.dto.price;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriceResponseDto Tests")
class PriceResponseDtoTest {

    @Test
    @DisplayName("Should create PriceResponseDto with all valid fields")
    void shouldCreatePriceResponseDtoWithAllValidFields() {
        // Given
        Long productId = 35455L;
        Long brandId = 1L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        BigDecimal price = new BigDecimal("35.50");

        // When
        PriceResponseDto dto = new PriceResponseDto(
                productId, brandId, priceList, startDate, endDate, price);

        // Then
        assertEquals(productId, dto.productId());
        assertEquals(brandId, dto.brandId());
        assertEquals(priceList, dto.priceList());
        assertEquals(startDate, dto.startDate());
        assertEquals(endDate, dto.endDate());
        assertEquals(price, dto.price());
    }

    @Test
    @DisplayName("Should create PriceResponseDto with null values")
    void shouldCreatePriceResponseDtoWithNullValues() {
        // When
        PriceResponseDto dto = new PriceResponseDto(null, null, null, null, null, null);

        // Then
        assertNull(dto.productId());
        assertNull(dto.brandId());
        assertNull(dto.priceList());
        assertNull(dto.startDate());
        assertNull(dto.endDate());
        assertNull(dto.price());
    }

    @Test
    @DisplayName("Should implement equals correctly for same values")
    void shouldImplementEqualsCorrectlyForSameValues() {
        // Given
        Long productId = 35455L;
        Long brandId = 1L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        BigDecimal price = new BigDecimal("35.50");

        PriceResponseDto dto1 = new PriceResponseDto(
                productId, brandId, priceList, startDate, endDate, price);
        PriceResponseDto dto2 = new PriceResponseDto(
                productId, brandId, priceList, startDate, endDate, price);

        // Then
        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Should have meaningful toString representation")
    void shouldHaveMeaningfulToStringRepresentation() {
        // Given
        Long productId = 35455L;
        Long brandId = 1L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        BigDecimal price = new BigDecimal("35.50");

        PriceResponseDto dto = new PriceResponseDto(
                productId, brandId, priceList, startDate, endDate, price);

        // When
        String toString = dto.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("PriceResponseDto"));
        assertTrue(toString.contains("35455"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("35.50"));
    }
  
}
