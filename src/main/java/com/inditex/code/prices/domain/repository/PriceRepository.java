package com.inditex.code.prices.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.inditex.code.prices.domain.dto.price.PriceDto;

public interface PriceRepository {

    public List<PriceDto> findAll();
    public List<PriceDto> findByFilters(LocalDateTime activeDate, Long productId, Long brandId);

}
