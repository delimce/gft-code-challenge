package com.inditex.code.prices.application.services.price;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.mapper.PriceMapper;
import com.inditex.code.prices.domain.port.PricePort;
import com.inditex.code.prices.domain.repository.PriceRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceService implements PricePort {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    public PriceService(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<PriceResponseDto> getPrices() {
        List<PriceDto> prices = priceRepository.findAll();
        return priceMapper.dtoListToResponseDtoList(prices);
    }

    @Override
    public List<PriceResponseDto> getPricesFiltered(LocalDateTime activeDate, Long productId, Long brandId) {
        List<PriceDto> prices = priceRepository.findByFilters(activeDate, productId, brandId);
        return priceMapper.dtoListToResponseDtoList(prices);
    }
}
