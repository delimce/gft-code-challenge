package com.inditex.code.prices.e2e;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = { "server.port=8083" })
public class KarateTests {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("baseUrl", () -> "http://localhost:8083/v1");
    }

    @Test
    void testParallel() {
        System.setProperty("karate.env", "dev");
        System.setProperty("baseUrl", "http://localhost:8083/v1");
        Results results = Runner.path("classpath:com/inditex/code/prices/e2e/prices")
                .reportDir("target/karate-reports")
                .parallel(1);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
