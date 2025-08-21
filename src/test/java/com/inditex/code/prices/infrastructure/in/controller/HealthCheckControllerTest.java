package com.inditex.code.prices.infrastructure.in.controller;

import com.inditex.code.prices.domain.dto.health.HealthStatus;
import com.inditex.code.prices.domain.port.HealthCheckPort;
import com.inditex.code.prices.domain.mapper.HealthMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HealthCheckPort healthCheckPort;

    @MockitoBean
    private HealthMapper healthMapper;

    @Test
    void health_returnsUp() throws Exception {
        // Given
        HealthStatus domainStatus = HealthStatus.up();
        com.inditex.code.prices.api.model.HealthStatus apiStatus = new com.inditex.code.prices.api.model.HealthStatus()
                .status(com.inditex.code.prices.api.model.HealthStatus.StatusEnum.UP);

        when(healthCheckPort.health()).thenReturn(domainStatus);
        when(healthMapper.toApiModel(domainStatus)).thenReturn(apiStatus);

        // When & Then
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    void health_returnsDown() throws Exception {
        // Given
        HealthStatus domainStatus = HealthStatus.down();
        com.inditex.code.prices.api.model.HealthStatus apiStatus = new com.inditex.code.prices.api.model.HealthStatus()
                .status(com.inditex.code.prices.api.model.HealthStatus.StatusEnum.DOWN);

        when(healthCheckPort.health()).thenReturn(domainStatus);
        when(healthMapper.toApiModel(domainStatus)).thenReturn(apiStatus);

        // When & Then
        mockMvc.perform(get("/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status", is("DOWN")));
    }
}
