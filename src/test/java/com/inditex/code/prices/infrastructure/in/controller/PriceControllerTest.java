package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.application.services.price.validation.PriceFilterValidator;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.port.PricePort;
import com.inditex.code.prices.domain.mapper.PriceMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
class PriceControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private PricePort pricePort;

        @MockitoBean
        private PriceFilterValidator validator;

        @MockitoBean
        private PriceMapper priceMapper;

        @Test
        void getPrices_shouldReturnListOfPrices() throws Exception {
                // Given
                PriceResponseDto price1 = new PriceResponseDto(35455L, 1L, 1,
                                LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59),
                                new BigDecimal("35.50"));

                PriceResponseDto price2 = new PriceResponseDto(35455L, 1L, 2,
                                LocalDateTime.of(2020, 6, 14, 15, 0),
                                LocalDateTime.of(2020, 6, 14, 18, 30),
                                new BigDecimal("25.45"));

                List<PriceResponseDto> prices = Arrays.asList(price1, price2);

                // Create API model responses that the mapper would return
                com.inditex.code.prices.api.model.PriceResponse apiPrice1 = new com.inditex.code.prices.api.model.PriceResponse()
                                .productId(35455L)
                                .brandId(1L)
                                .priceList(1)
                                .price(35.50);

                com.inditex.code.prices.api.model.PriceResponse apiPrice2 = new com.inditex.code.prices.api.model.PriceResponse()
                                .productId(35455L)
                                .brandId(1L)
                                .priceList(2)
                                .price(25.45);

                List<com.inditex.code.prices.api.model.PriceResponse> apiPrices = Arrays.asList(apiPrice1, apiPrice2);

                when(pricePort.getPrices()).thenReturn(prices);
                when(priceMapper.toApiModelList(prices)).thenReturn(apiPrices);

                // When & Then
                mockMvc.perform(get("/prices"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].productId", is(35455)))
                                .andExpect(jsonPath("$[0].brandId", is(1)))
                                .andExpect(jsonPath("$[0].priceList", is(1)))
                                .andExpect(jsonPath("$[0].price", is(35.50)))
                                .andExpect(jsonPath("$[1].productId", is(35455)))
                                .andExpect(jsonPath("$[1].brandId", is(1)))
                                .andExpect(jsonPath("$[1].priceList", is(2)))
                                .andExpect(jsonPath("$[1].price", is(25.45)));
        }
}
