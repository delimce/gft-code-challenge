package com.inditex.code.prices.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPrices_shouldReturnPricesFromDatabase() throws Exception {
        // When & Then - Using the sample data from V1__Create_initial_schema.sql
        mockMvc.perform(get("/prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                // First price record
                .andExpect(jsonPath("$[0].brandId", is(1)))
                .andExpect(jsonPath("$[0].productId", is(35455)))
                .andExpect(jsonPath("$[0].priceList", is(1)))
                .andExpect(jsonPath("$[0].priority", is(0)))
                .andExpect(jsonPath("$[0].price", is(35.50)))
                .andExpect(jsonPath("$[0].currency", is("EUR")))
                // Second price record
                .andExpect(jsonPath("$[1].brandId", is(1)))
                .andExpect(jsonPath("$[1].productId", is(35455)))
                .andExpect(jsonPath("$[1].priceList", is(2)))
                .andExpect(jsonPath("$[1].priority", is(1)))
                .andExpect(jsonPath("$[1].price", is(25.45)))
                .andExpect(jsonPath("$[1].currency", is("EUR")))
                // Third price record
                .andExpect(jsonPath("$[2].brandId", is(1)))
                .andExpect(jsonPath("$[2].productId", is(35455)))
                .andExpect(jsonPath("$[2].priceList", is(3)))
                .andExpect(jsonPath("$[2].priority", is(1)))
                .andExpect(jsonPath("$[2].price", is(30.50)))
                .andExpect(jsonPath("$[2].currency", is("EUR")))
                // Fourth price record
                .andExpect(jsonPath("$[3].brandId", is(1)))
                .andExpect(jsonPath("$[3].productId", is(35455)))
                .andExpect(jsonPath("$[3].priceList", is(4)))
                .andExpect(jsonPath("$[3].priority", is(1)))
                .andExpect(jsonPath("$[3].price", is(38.95)))
                .andExpect(jsonPath("$[3].currency", is("EUR")));
    }
}
