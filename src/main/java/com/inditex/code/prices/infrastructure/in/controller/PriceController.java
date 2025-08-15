package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.port.PricePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for price operations.
 */
@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PricePort pricePort;

    public PriceController(PricePort pricePort) {
        this.pricePort = pricePort;
    }

    /**
     * Get all prices.
     * 
     * @return list of prices
     */
    @GetMapping
    public ResponseEntity<List<PriceDto>> getPrices() {
        List<PriceDto> prices = pricePort.getPrices();
        return ResponseEntity.ok(prices);
    }
}
