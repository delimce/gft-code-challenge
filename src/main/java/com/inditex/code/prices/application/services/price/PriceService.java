package com.inditex.code.prices.application.services.price;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.port.PricePort;
import com.inditex.code.prices.infrastructure.out.persistence.mapper.PriceMapper;
import com.inditex.code.prices.infrastructure.out.persistence.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementing price operations.
 */
@Service
public class PriceService implements PricePort {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    public PriceService(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<PriceDto> getPrices() {
        return priceMapper.toDtoList(priceRepository.findAll());
    }
}
