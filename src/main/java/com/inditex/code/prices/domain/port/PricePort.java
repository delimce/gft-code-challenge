package com.inditex.code.prices.domain.port;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Input port for price operations.
 */
public interface PricePort {

    /**
     * Retrieves all prices from the system.
     * 
     * @return list of price DTOs
     */
    List<PriceDto> getPrices();

    /**
     * Retrieves filtered prices from the system.
     * 
     * @param activeDate date when the product is active for sale (between start and
     *                   end dates)
     * @param productId  ID of the product to filter by
     * @param brandId    ID of the brand to filter by
     * @return list of filtered price DTOs
     */
    List<PriceDto> getPricesFiltered(LocalDateTime activeDate, Long productId, Long brandId);
}
