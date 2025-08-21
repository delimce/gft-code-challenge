package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.api.PricesApi;
import com.inditex.code.prices.api.model.PriceResponse;
import com.inditex.code.prices.application.services.price.validation.PriceFilterValidator;
import com.inditex.code.prices.domain.dto.price.PriceFilterRequestDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.port.PricePort;
import com.inditex.code.prices.domain.mapper.PriceMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class PriceController implements PricesApi {

    private final PricePort pricePort;
    private final PriceFilterValidator validator;
    private final PriceMapper priceMapper;

    public PriceController(PricePort pricePort, PriceFilterValidator validator, PriceMapper priceMapper) {
        this.pricePort = pricePort;
        this.validator = validator;
        this.priceMapper = priceMapper;
    }

    @Override
    public ResponseEntity<List<PriceResponse>> _getPrices(OffsetDateTime activeDate, Long productId, Long brandId) {
        // Convert OffsetDateTime to LocalDateTime for internal processing
        LocalDateTime localActiveDate = activeDate != null ? activeDate.toLocalDateTime() : null;

        // Create and validate the filter request
        PriceFilterRequestDto filterRequest = new PriceFilterRequestDto(localActiveDate, productId, brandId);
        validator.validate(filterRequest);

        // If any filter parameter is provided, use filtered search
        boolean hasFilters = activeDate != null || productId != null || brandId != null;

        List<PriceResponseDto> prices = (hasFilters) ? pricePort.getPricesFiltered(localActiveDate, productId, brandId)
                : pricePort.getPrices();

        // Convert to API models using the mapper
        List<PriceResponse> apiPrices = priceMapper.toApiModelList(prices);

        return ResponseEntity.ok(apiPrices);
    }
}
