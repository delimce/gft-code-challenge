package com.inditex.code.prices.domain.port;

import com.inditex.code.prices.domain.dto.price.PriceResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PricePort {

    /**
     * Retrieves all prices from the system.
     * 
     * @return list of price DTOs
     */
    List<PriceResponseDto> getPrices();

    /**
     * Retrieves filtered prices from the system.
     * 
     * @param activeDate date when the product is active for sale (between start and
     *                   end dates)
     * @param productId  ID of the product to filter by
     * @param brandId    ID of the brand to filter by
     * @return list of filtered price DTOs
     */
    List<PriceResponseDto> getPricesFiltered(LocalDateTime activeDate, Long productId, Long brandId);
}
