package com.inditex.code.prices.domain.dto.price;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriceDto Tests")
class PriceDtoTest {

    @Test
    @DisplayName("Should create PriceDto with all valid fields")
    void shouldCreatePriceDtoWithAllValidFields() {
        // Given
        Long id = 1L;
        Long brandId = 1L;
        Long productId = 35455L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        Integer priority = 0;
        BigDecimal price = new BigDecimal("35.50");
        String currency = "EUR";

        // When
        PriceDto dto = new PriceDto(id, brandId, productId, priceList,
                startDate, endDate, priority, price, currency);

        // Then
        assertEquals(id, dto.id());
        assertEquals(brandId, dto.brandId());
        assertEquals(productId, dto.productId());
        assertEquals(priceList, dto.priceList());
        assertEquals(startDate, dto.startDate());
        assertEquals(endDate, dto.endDate());
        assertEquals(priority, dto.priority());
        assertEquals(price, dto.price());
        assertEquals(currency, dto.currency());
    }

    @Test
    @DisplayName("Should create PriceDto with all null values")
    void shouldCreatePriceDtoWithAllNullValues() {
        // When
        PriceDto dto = new PriceDto(null, null, null, null, null, null, null, null, null);

        // Then
        assertNull(dto.id());
        assertNull(dto.brandId());
        assertNull(dto.productId());
        assertNull(dto.priceList());
        assertNull(dto.startDate());
        assertNull(dto.endDate());
        assertNull(dto.priority());
        assertNull(dto.price());
        assertNull(dto.currency());
    }

    @Test
    @DisplayName("Should implement equals correctly for same values")
    void shouldImplementEqualsCorrectlyForSameValues() {
        // Given
        Long id = 1L;
        Long brandId = 1L;
        Long productId = 35455L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        Integer priority = 0;
        BigDecimal price = new BigDecimal("35.50");
        String currency = "EUR";

        PriceDto dto1 = new PriceDto(id, brandId, productId, priceList,
                startDate, endDate, priority, price, currency);
        PriceDto dto2 = new PriceDto(id, brandId, productId, priceList,
                startDate, endDate, priority, price, currency);

        // Then
        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Should implement equals correctly for different values")
    void shouldImplementEqualsCorrectlyForDifferentValues() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        BigDecimal price = new BigDecimal("35.50");
        String currency = "EUR";

        PriceDto dto1 = new PriceDto(1L, 1L, 35455L, 1,
                startDate, endDate, 0, price, currency);
        PriceDto dto2 = new PriceDto(2L, 1L, 35455L, 1,
                startDate, endDate, 0, price, currency);

        // Then
        assertNotEquals(dto1, dto2);
    }


    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        // Given
        Long id = 1L;
        Long brandId = 1L;
        Long productId = 35455L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        Integer priority = 0;
        BigDecimal price = new BigDecimal("35.50");
        String currency = "EUR";

        // When
        PriceDto dto = new PriceDto(id, brandId, productId, priceList,
                startDate, endDate, priority, price, currency);

        // Then - Records are immutable by design, verify values remain constant
        assertEquals(id, dto.id());
        assertEquals(brandId, dto.brandId());
        assertEquals(productId, dto.productId());
        assertEquals(priceList, dto.priceList());
        assertEquals(startDate, dto.startDate());
        assertEquals(endDate, dto.endDate());
        assertEquals(priority, dto.priority());
        assertEquals(price, dto.price());
        assertEquals(currency, dto.currency());

        // Verify values haven't changed after multiple accesses
        assertEquals(id, dto.id());
        assertEquals(brandId, dto.brandId());
        assertEquals(productId, dto.productId());
        assertEquals(priceList, dto.priceList());
        assertEquals(startDate, dto.startDate());
        assertEquals(endDate, dto.endDate());
        assertEquals(priority, dto.priority());
        assertEquals(price, dto.price());
        assertEquals(currency, dto.currency());
    }
}
