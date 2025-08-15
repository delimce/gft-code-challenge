package com.inditex.code.prices.infrastructure.out.adapter;

import com.inditex.code.prices.domain.repository.PriceRepository;
import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.mapper.PriceMapper;
import com.inditex.code.prices.infrastructure.out.persistence.jpa.repository.PriceJpaRepository;
import com.inditex.code.prices.infrastructure.out.persistence.jpa.entity.PriceEntity;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PriceRepositoryImpl implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;
    private final PriceMapper priceMapper;

    public PriceRepositoryImpl(PriceJpaRepository priceJpaRepository, PriceMapper priceMapper) {
        this.priceJpaRepository = priceJpaRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<PriceDto> findAll() {
        List<PriceEntity> priceEntities = priceJpaRepository.findAll();
        return priceEntities.stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PriceDto> findByFilters(LocalDateTime activeDate, Long productId, Long brandId) {
        List<PriceEntity> priceEntities = priceJpaRepository.findByFilters(activeDate, productId, brandId);
        return priceEntities.stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
    }

}
