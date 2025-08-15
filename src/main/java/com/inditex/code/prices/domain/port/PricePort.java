package com.inditex.code.prices.domain.port;

import com.inditex.code.prices.domain.dto.price.PriceDto;
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
}
