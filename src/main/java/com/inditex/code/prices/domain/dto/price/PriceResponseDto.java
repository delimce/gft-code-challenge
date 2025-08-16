package com.inditex.code.prices.domain.dto.price;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Price information for a product")
public record PriceResponseDto(
                @Schema(description = "Product identifier", example = "35455") 
                Long productId,

                @Schema(description = "Brand identifier", example = "1") 
                Long brandId,

                @Schema(description = "Price list identifier", example = "1") 
                Integer priceList,

                @Schema(description = "Price validity start date", example = "2020-06-14T00:00:00") 
                LocalDateTime startDate,

                @Schema(description = "Price validity end date", example = "2020-12-31T23:59:59") 
                LocalDateTime endDate,

                @Schema(description = "Product price", example = "35.50") 
                BigDecimal price) {

}
