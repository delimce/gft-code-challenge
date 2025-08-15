package com.inditex.code.prices.infrastructure.in.validation;

import com.inditex.code.prices.application.services.price.validation.PriceFilterValidator;
import com.inditex.code.prices.domain.dto.price.PriceFilterRequestDto;
import com.inditex.code.prices.domain.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PriceFilterValidatorTest {

    private final PriceFilterValidator validator = new PriceFilterValidator();

    @Test
    void testValidRequestPasses() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(
                LocalDateTime.now(),
                1L,
                2L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void testNullValuesPassValidation() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(null, null, null);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void testInvalidRequestsFailValidation(PriceFilterRequestDto request, String expectedErrorMessage) {
        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertTrue(exception.getErrors().stream()
                .anyMatch(error -> error.contains(expectedErrorMessage)),
                "Expected error message not found: " + expectedErrorMessage);
    }

    private static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(
                        new PriceFilterRequestDto(LocalDateTime.now().plusYears(2), 1L, 1L),
                        "Active date cannot be more than one year in the future"),
                Arguments.of(
                        new PriceFilterRequestDto(LocalDateTime.now(), -1L, 1L),
                        "Product ID must be a positive number"),
                Arguments.of(
                        new PriceFilterRequestDto(LocalDateTime.now(), 1L, -5L),
                        "Brand ID must be a positive number"),
                Arguments.of(
                        new PriceFilterRequestDto(LocalDateTime.now(), 0L, 0L),
                        "Product ID must be a positive number"));
    }
}
