package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.application.services.price.validation.PriceFilterValidator;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.domain.port.PricePort;
import com.inditex.code.prices.infrastructure.in.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerValidationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public PricePort mockPricePort() {
            return Mockito.mock(PricePort.class);
        }

        @Bean
        @Primary
        public PriceFilterValidator mockValidator() {
            return Mockito.mock(PriceFilterValidator.class);
        }

        @Bean
        public GlobalExceptionHandler exceptionHandler() {
            return new GlobalExceptionHandler();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PricePort pricePort;

    @Test
    void testValidRequestParams() throws Exception {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PriceResponseDto priceResponseDto = new PriceResponseDto(
                35455L, 1L, 1, now, now.plusDays(1),
                new BigDecimal("35.50"));

        when(pricePort.getPricesFiltered(any(), eq(35455L), eq(1L)))
                .thenReturn(List.of(priceResponseDto));

        // When & Then
        mockMvc.perform(get("/prices")
                .param("activeDate", now.toString())
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    @Test
    void testInvalidDateFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/prices")
                .param("activeDate", "2020-06-14-invalid")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testInvalidProductIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/prices")
                .param("productId", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testNoResults() throws Exception {
        // Given
        when(pricePort.getPricesFiltered(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // When & Then
        LocalDateTime now = LocalDateTime.now();
        mockMvc.perform(get("/prices")
                .param("activeDate", now.toString())
                .param("productId", "99999")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
