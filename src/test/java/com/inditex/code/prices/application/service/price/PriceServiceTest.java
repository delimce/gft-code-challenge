package com.inditex.code.prices.application.service.price;

import com.inditex.code.prices.application.services.price.PriceService;
import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.mapper.PriceMapper;
import com.inditex.code.prices.domain.repository.PriceRepository;

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
                PriceDto dto1 = createPriceDto(1L, 1L, 35455L, 1,
                                LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                0, new BigDecimal("35.50"), "EUR");

                PriceDto dto2 = createPriceDto(2L, 1L, 35455L, 2,
                                LocalDateTime.of(2020, 6, 14, 15, 0),
                                LocalDateTime.of(2020, 6, 14, 18, 30),
                                1, new BigDecimal("25.45"), "EUR");

                PriceResponseDto responseDto1 = new PriceResponseDto(35455L, 1L, 1,
                                LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                new BigDecimal("35.50"));

                PriceResponseDto responseDto2 = new PriceResponseDto(35455L, 1L, 2,
                                LocalDateTime.of(2020, 6, 14, 15, 0),
                                LocalDateTime.of(2020, 6, 14, 18, 30),
                                new BigDecimal("25.45"));

                List<PriceDto> dtos = Arrays.asList(dto1, dto2);
                List<PriceResponseDto> expectedResponseDtos = Arrays.asList(responseDto1, responseDto2);

                when(priceRepository.findAll()).thenReturn(dtos);
                when(priceMapper.dtoListToResponseDtoList(dtos)).thenReturn(expectedResponseDtos);

                // When
                List<PriceResponseDto> result = priceService.getPrices();

                // Then
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals(responseDto1, result.get(0));
                assertEquals(responseDto2, result.get(1));
        }

        @Test
        void getPricesFiltered_shouldReturnFilteredPrices() {
                // Given
                LocalDateTime activeDate = LocalDateTime.of(2020, 6, 14, 10, 0);
                Long productId = 35455L;
                Long brandId = 1L;

                PriceDto filteredDto = createPriceDto(1L, 1L, 35455L, 1,
                                LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                0, new BigDecimal("35.50"), "EUR");

                PriceResponseDto expectedResponseDto = new PriceResponseDto(35455L, 1L, 1,
                                LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                new BigDecimal("35.50"));

                List<PriceDto> filteredDtos = Arrays.asList(filteredDto);
                List<PriceResponseDto> expectedResponseDtos = Arrays.asList(expectedResponseDto);

                when(priceRepository.findByFilters(activeDate, productId, brandId)).thenReturn(filteredDtos);
                when(priceMapper.dtoListToResponseDtoList(filteredDtos)).thenReturn(expectedResponseDtos);

                // When
                List<PriceResponseDto> result = priceService.getPricesFiltered(activeDate, productId, brandId);

                // Then
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(expectedResponseDto, result.get(0));
        }

        private PriceDto createPriceDto(Long id, Long brandId, Long productId, Integer priceList,
                        LocalDateTime startDate, LocalDateTime endDate,
                        Integer priority, BigDecimal price, String currency) {
                return new PriceDto(id, brandId, productId, priceList, startDate, endDate, priority, price, currency);
        }
}
