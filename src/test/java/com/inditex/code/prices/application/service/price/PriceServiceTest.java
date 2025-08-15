package com.inditex.code.prices.application.service.price;

import com.inditex.code.prices.application.services.price.PriceService;
import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.infrastructure.out.persistence.entity.PriceEntity;
import com.inditex.code.prices.infrastructure.out.persistence.mapper.PriceMapper;
import com.inditex.code.prices.infrastructure.out.persistence.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private PriceMapper priceMapper;

    private PriceService priceService;

    @BeforeEach
    void setUp() {
        priceService = new PriceService(priceRepository, priceMapper);
    }

    @Test
    void getPrices_shouldReturnListOfPrices() {
        // Given
        PriceEntity entity1 = createPriceEntity(1L, 1L, 35455L, 1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                0, new BigDecimal("35.50"), "EUR");

        PriceEntity entity2 = createPriceEntity(2L, 1L, 35455L, 2,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                1, new BigDecimal("25.45"), "EUR");

        PriceDto dto1 = createPriceDto(1L, 1L, 35455L, 1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                0, new BigDecimal("35.50"), "EUR");

        PriceDto dto2 = createPriceDto(2L, 1L, 35455L, 2,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                1, new BigDecimal("25.45"), "EUR");

        List<PriceEntity> entities = Arrays.asList(entity1, entity2);
        List<PriceDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(priceRepository.findAll()).thenReturn(entities);
        when(priceMapper.toDtoList(entities)).thenReturn(expectedDtos);

        // When
        List<PriceDto> result = priceService.getPrices();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    private PriceEntity createPriceEntity(Long id, Long brandId, Long productId, Integer priceList,
            LocalDateTime startDate, LocalDateTime endDate,
            Integer priority, BigDecimal price, String currency) {
        PriceEntity entity = new PriceEntity();
        entity.setId(id);
        entity.setBrandId(brandId);
        entity.setProductId(productId);
        entity.setPriceList(priceList);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setPriority(priority);
        entity.setPrice(price);
        entity.setCurrency(currency);
        return entity;
    }

    private PriceDto createPriceDto(Long id, Long brandId, Long productId, Integer priceList,
            LocalDateTime startDate, LocalDateTime endDate,
            Integer priority, BigDecimal price, String currency) {
        return new PriceDto(id, brandId, productId, priceList, startDate, endDate, priority, price, currency);
    }
}
