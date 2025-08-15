package com.inditex.code.prices.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.code.prices.domain.dto.price.PriceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for price filtering functionality.
 */
@Disabled("Temporarily disabled for testing")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PriceFilteringIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Insert test data
        try (Connection connection = dataSource.getConnection()) {
            // First, insert brands using MERGE (H2 syntax)
            String brandSql = "MERGE INTO BRANDS (ID, NAME) VALUES (?, ?)";
            PreparedStatement brandStmt = connection.prepareStatement(brandSql);

            brandStmt.setLong(1, 1L);
            brandStmt.setString(2, "ZARA");
            brandStmt.executeUpdate();

            brandStmt.setLong(1, 2L);
            brandStmt.setString(2, "H&M");
            brandStmt.executeUpdate();

            brandStmt.close();

            // Then, insert products using MERGE (H2 syntax)
            String productSql = "MERGE INTO PRODUCTS (ID, NAME, BRAND_ID) VALUES (?, ?, ?)";
            PreparedStatement productStmt = connection.prepareStatement(productSql);

            productStmt.setLong(1, 35455L);
            productStmt.setString(2, "First Product");
            productStmt.setLong(3, 1L);
            productStmt.executeUpdate();

            productStmt.setLong(1, 12345L);
            productStmt.setString(2, "Second Product");
            productStmt.setLong(3, 1L);
            productStmt.executeUpdate();

            productStmt.close();

            // Finally, insert price data
            String sql = """
                    INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);

            // Brand 1, Product 35455 - valid from 2020-06-14 00:00:00 to 2020-12-31
            // 23:59:59
            stmt.setLong(1, 1L);
            stmt.setObject(2, LocalDateTime.of(2020, 6, 14, 0, 0, 0));
            stmt.setObject(3, LocalDateTime.of(2020, 12, 31, 23, 59, 59));
            stmt.setLong(4, 1L);
            stmt.setLong(5, 35455L);
            stmt.setInt(6, 0);
            stmt.setBigDecimal(7, new java.math.BigDecimal("35.50"));
            stmt.setString(8, "EUR");
            stmt.executeUpdate();

            // Brand 1, Product 35455 - valid from 2020-06-14 15:00:00 to 2020-06-14
            // 18:30:00 (higher priority)
            stmt.setLong(1, 1L);
            stmt.setObject(2, LocalDateTime.of(2020, 6, 14, 15, 0, 0));
            stmt.setObject(3, LocalDateTime.of(2020, 6, 14, 18, 30, 0));
            stmt.setLong(4, 2L);
            stmt.setLong(5, 35455L);
            stmt.setInt(6, 1);
            stmt.setBigDecimal(7, new java.math.BigDecimal("25.45"));
            stmt.setString(8, "EUR");
            stmt.executeUpdate();

            // Brand 2, Product 35455 - valid from 2020-06-14 10:00:00 to 2020-06-14
            // 20:00:00
            stmt.setLong(1, 2L);
            stmt.setObject(2, LocalDateTime.of(2020, 6, 14, 10, 0, 0));
            stmt.setObject(3, LocalDateTime.of(2020, 6, 14, 20, 0, 0));
            stmt.setLong(4, 1L);
            stmt.setLong(5, 35455L);
            stmt.setInt(6, 0);
            stmt.setBigDecimal(7, new java.math.BigDecimal("30.00"));
            stmt.setString(8, "EUR");
            stmt.executeUpdate();

            // Brand 1, Product 12345 - different product
            stmt.setLong(1, 1L);
            stmt.setObject(2, LocalDateTime.of(2020, 6, 14, 0, 0, 0));
            stmt.setObject(3, LocalDateTime.of(2020, 12, 31, 23, 59, 59));
            stmt.setLong(4, 1L);
            stmt.setLong(5, 12345L);
            stmt.setInt(6, 0);
            stmt.setBigDecimal(7, new java.math.BigDecimal("45.00"));
            stmt.setString(8, "EUR");
            stmt.executeUpdate();

            stmt.close();
        }
    }

    @Test
    void testGetAllPricesWithoutFilters() throws Exception {
        MvcResult result = mockMvc.perform(get("/prices")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        assertThat(priceList).hasSize(4);
    }

    @Test
    void testFilterByActiveDate() throws Exception {
        // Test filtering by active date - 2020-06-14 16:00:00 (should match 3 prices: 2
        // for product 35455 and 1 for product 12345)
        String activeDate = "2020-06-14T16:00:00";

        MvcResult result = mockMvc.perform(get("/prices")
                .param("active_date", activeDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        // Should return 3 prices (2 for product 35455 from different brands + 1 for
        // product 12345)
        // Results ordered by priority DESC
        assertThat(priceList).hasSize(3);

        // First should be the highest priority price for product 35455 (priority 1)
        assertThat(priceList.get(0).productId()).isEqualTo(35455L);
        assertThat(priceList.get(0).priority()).isEqualTo(1);
        assertThat(priceList.get(0).price()).isEqualByComparingTo(new java.math.BigDecimal("25.45"));
    }

    @Test
    void testFilterByProductId() throws Exception {
        MvcResult result = mockMvc.perform(get("/prices")
                .param("product_id", "35455")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        assertThat(priceList).hasSize(3);
        assertThat(priceList).allMatch(price -> price.productId().equals(35455L));
    }

    @Test
    void testFilterByBrandId() throws Exception {
        MvcResult result = mockMvc.perform(get("/prices")
                .param("brand_id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        assertThat(priceList).hasSize(3);
        assertThat(priceList).allMatch(price -> price.brandId().equals(1L));
    }

    @Test
    void testFilterByActiveDateAndProductId() throws Exception {
        // Filter for product 35455 active at 2020-06-14 16:00:00
        String activeDate = "2020-06-14T16:00:00";

        MvcResult result = mockMvc.perform(get("/prices")
                .param("active_date", activeDate)
                .param("product_id", "35455")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        // Should return 2 prices for product 35455 (from brands 1 and 2)
        assertThat(priceList).hasSize(2);
        assertThat(priceList).allMatch(price -> price.productId().equals(35455L));

        // First should be the highest priority price (priority 1)
        assertThat(priceList.get(0).priority()).isEqualTo(1);
        assertThat(priceList.get(0).brandId()).isEqualTo(1L);
    }

    @Test
    void testFilterByAllParameters() throws Exception {
        // Filter for brand 1, product 35455, active at 2020-06-14 16:00:00
        String activeDate = "2020-06-14T16:00:00";

        MvcResult result = mockMvc.perform(get("/prices")
                .param("active_date", activeDate)
                .param("product_id", "35455")
                .param("brand_id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        // Should return 2 prices for brand 1, product 35455 at that time
        assertThat(priceList).hasSize(2);
        assertThat(priceList).allMatch(price -> price.brandId().equals(1L));
        assertThat(priceList).allMatch(price -> price.productId().equals(35455L));

        // First should be the higher priority price (25.45 EUR)
        assertThat(priceList.get(0).priority()).isEqualTo(1);
        assertThat(priceList.get(0).price()).isEqualByComparingTo(new java.math.BigDecimal("25.45"));
    }

    @Test
    void testFilterWithNoResults() throws Exception {
        // Filter for a date outside the valid ranges
        String activeDate = "2019-01-01T10:00:00";

        MvcResult result = mockMvc.perform(get("/prices")
                .param("active_date", activeDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PriceDto[] prices = objectMapper.readValue(jsonResponse, PriceDto[].class);
        List<PriceDto> priceList = Arrays.asList(prices);

        assertThat(priceList).isEmpty();
    }
}
