package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.application.services.price.validation.PriceFilterValidator;
import com.inditex.code.prices.domain.dto.price.PriceFilterRequestDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.port.PricePort;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PricePort pricePort;
    private final PriceFilterValidator validator;

    public PriceController(PricePort pricePort, PriceFilterValidator validator) {
        this.pricePort = pricePort;
        this.validator = validator;
    }

    /**
     * Get prices with optional filtering by active date, product ID, and brand ID.
     * 
     * @param activeDate date when the product is active for sale (between start and
     *                   end dates)
     * @param productId  ID of the product to filter by
     * @param brandId    ID of the brand to filter by
     * @return list of prices
     */
    @GetMapping
    public ResponseEntity<List<PriceResponseDto>> getPrices(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime activeDate,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long brandId) {

        // Create and validate the filter request
        PriceFilterRequestDto filterRequest = new PriceFilterRequestDto(activeDate, productId, brandId);
        validator.validate(filterRequest);

        // If any filter parameter is provided, use filtered search
        boolean hasFilters = activeDate != null || productId != null || brandId != null;

        List<PriceResponseDto> prices = (hasFilters) ? pricePort.getPricesFiltered(activeDate, productId, brandId)
                : pricePort.getPrices();

        return ResponseEntity.ok(prices);
    }
}
