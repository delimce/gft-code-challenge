package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.application.services.price.validation.PriceFilterValidator;
import com.inditex.code.prices.domain.dto.price.PriceFilterRequestDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.port.PricePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Prices", description = "Operations related to product pricing")
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
     * @param activeDate date when the product is active for sale (between start and end dates)
     * @param productId  ID of the product to filter by
     * @param brandId    ID of the brand to filter by
     * @return list of prices
     */
    @Operation(summary = "Get product prices", description = "Retrieve product prices with optional filtering by active date, product ID, and brand ID. When no filters are applied, returns all available prices.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prices retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PriceResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid filter parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PriceResponseDto>> getPrices(
            @Parameter(description = "Active date to filter prices (ISO 8601 format: yyyy-MM-ddTHH:mm:ss)", example = "2020-06-14T10:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime activeDate,

            @Parameter(description = "Product ID to filter by", example = "35455") 
            @RequestParam(required = false) Long productId,

            @Parameter(description = "Brand ID to filter by", example = "1") 
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
