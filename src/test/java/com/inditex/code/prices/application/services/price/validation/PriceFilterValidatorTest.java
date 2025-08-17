package com.inditex.code.prices.application.services.price.validation;

import com.inditex.code.prices.domain.dto.price.PriceFilterRequestDto;
import com.inditex.code.prices.domain.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriceFilterValidator Tests")
class PriceFilterValidatorTest {

    private PriceFilterValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PriceFilterValidator();
    }

    @Test
    @DisplayName("Should pass validation with all valid fields")
    void shouldPassValidationWithAllValidFields() {
        // Given
        LocalDateTime validDate = LocalDateTime.now().plusMonths(6);
        PriceFilterRequestDto request = new PriceFilterRequestDto(validDate, 35455L, 1L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with all null fields")
    void shouldPassValidationWithAllNullFields() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(null, null, null);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with only activeDate provided")
    void shouldPassValidationWithOnlyActiveDateProvided() {
        // Given
        LocalDateTime validDate = LocalDateTime.now().plusDays(30);
        PriceFilterRequestDto request = new PriceFilterRequestDto(validDate, null, null);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with only productId provided")
    void shouldPassValidationWithOnlyProductIdProvided() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(null, 35455L, null);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with only brandId provided")
    void shouldPassValidationWithOnlyBrandIdProvided() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(null, null, 1L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with current date")
    void shouldPassValidationWithCurrentDate() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        PriceFilterRequestDto request = new PriceFilterRequestDto(now, 1L, 1L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with date exactly one year in the future")
    void shouldPassValidationWithDateExactlyOneYearInFuture() {
        // Given
        LocalDateTime oneYearFromNow = LocalDateTime.now().plusYears(1);
        PriceFilterRequestDto request = new PriceFilterRequestDto(oneYearFromNow, 1L, 1L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should pass validation with past dates")
    void shouldPassValidationWithPastDates() {
        // Given
        LocalDateTime pastDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        PriceFilterRequestDto request = new PriceFilterRequestDto(pastDate, 1L, 1L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    @DisplayName("Should fail validation when activeDate is more than one year in the future")
    void shouldFailValidationWhenActiveDateIsMoreThanOneYearInFuture() {
        // Given
        LocalDateTime moreThanOneYearFromNow = LocalDateTime.now().plusYears(1).plusDays(1);
        PriceFilterRequestDto request = new PriceFilterRequestDto(moreThanOneYearFromNow, 1L, 1L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertEquals("Invalid price filter request", exception.getMessage());
        List<String> errors = exception.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Active date cannot be more than one year in the future", errors.get(0));
    }

    @Test
    @DisplayName("Should fail validation when productId is zero")
    void shouldFailValidationWhenProductIdIsZero() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), 0L, 1L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertEquals("Invalid price filter request", exception.getMessage());
        List<String> errors = exception.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Product ID must be a positive number", errors.get(0));
    }

    @Test
    @DisplayName("Should fail validation when productId is negative")
    void shouldFailValidationWhenProductIdIsNegative() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), -1L, 1L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertEquals("Invalid price filter request", exception.getMessage());
        List<String> errors = exception.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Product ID must be a positive number", errors.get(0));
    }

    @Test
    @DisplayName("Should fail validation when brandId is zero")
    void shouldFailValidationWhenBrandIdIsZero() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), 1L, 0L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertEquals("Invalid price filter request", exception.getMessage());
        List<String> errors = exception.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Brand ID must be a positive number", errors.get(0));
    }

    @Test
    @DisplayName("Should fail validation when brandId is negative")
    void shouldFailValidationWhenBrandIdIsNegative() {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), 1L, -5L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertEquals("Invalid price filter request", exception.getMessage());
        List<String> errors = exception.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Brand ID must be a positive number", errors.get(0));
    }

    @Test
    @DisplayName("Should collect multiple validation errors")
    void shouldCollectMultipleValidationErrors() {
        // Given
        LocalDateTime moreThanOneYearFromNow = LocalDateTime.now().plusYears(2);
        PriceFilterRequestDto request = new PriceFilterRequestDto(moreThanOneYearFromNow, -1L, -2L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertEquals("Invalid price filter request", exception.getMessage());
        List<String> errors = exception.getErrors();
        assertEquals(3, errors.size());
        assertTrue(errors.contains("Active date cannot be more than one year in the future"));
        assertTrue(errors.contains("Product ID must be a positive number"));
        assertTrue(errors.contains("Brand ID must be a positive number"));
    }

    @ParameterizedTest
    @DisplayName("Should pass validation with various valid productId values")
    @ValueSource(longs = {1L, 35455L, 99999L, Long.MAX_VALUE})
    void shouldPassValidationWithVariousValidProductIdValues(Long productId) {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), productId, 1L);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @ParameterizedTest
    @DisplayName("Should pass validation with various valid brandId values")
    @ValueSource(longs = {1L, 2L, 100L, 999L, Long.MAX_VALUE})
    void shouldPassValidationWithVariousValidBrandIdValues(Long brandId) {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), 35455L, brandId);

        // When & Then
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @ParameterizedTest
    @DisplayName("Should fail validation with various invalid productId values")
    @ValueSource(longs = {-1L, -100L, -999L, Long.MIN_VALUE, 0L})
    void shouldFailValidationWithVariousInvalidProductIdValues(Long productId) {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), productId, 1L);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertTrue(exception.getErrors().contains("Product ID must be a positive number"));
    }

    @ParameterizedTest
    @DisplayName("Should fail validation with various invalid brandId values")
    @ValueSource(longs = {-1L, -100L, -999L, Long.MIN_VALUE, 0L})
    void shouldFailValidationWithVariousInvalidBrandIdValues(Long brandId) {
        // Given
        PriceFilterRequestDto request = new PriceFilterRequestDto(LocalDateTime.now(), 35455L, brandId);

        // When
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request));

        // Then
        assertTrue(exception.getErrors().contains("Brand ID must be a positive number"));
    }

}
