package com.inditex.code.prices.domain.dto.price;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Request DTO for filtering prices.
 */
public record PriceFilterRequestDto(
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime activeDate,
                Long productId,
                Long brandId) {
}
