package com.inditex.code.prices.domain.dto.price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain DTO representing a price.
 */
public record PriceDto(
        Long id,
        Long brandId,
        Long productId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priority,
        BigDecimal price,
        String currency) {
}
