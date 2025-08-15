package com.inditex.code.prices.application.services.price.validation;

import com.inditex.code.prices.domain.dto.price.PriceFilterRequestDto;
import com.inditex.code.prices.domain.exception.ValidationException;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for price filter requests.
 */
@Component
public class PriceFilterValidator {

    /**
     * Validates a price filter request.
     *
     * @param request the request to validate
     * @throws ValidationException if validation fails
     */
    public void validate(PriceFilterRequestDto request) {
        List<String> errors = new ArrayList<>();

        // Validate activeDate (if provided)
        if (request.activeDate() != null) {
            validateActiveDate(request.activeDate(), errors);
        }

        // Validate productId (if provided)
        if (request.productId() != null) {
            validateProductId(request.productId(), errors);
        }

        // Validate brandId (if provided)
        if (request.brandId() != null) {
            validateBrandId(request.brandId(), errors);
        }

        // If there are validation errors, throw an exception
        if (!errors.isEmpty()) {
            throw new ValidationException("Invalid price filter request", errors);
        }
    }

    private void validateActiveDate(LocalDateTime activeDate, List<String> errors) {
        LocalDateTime now = LocalDateTime.now();
        if (activeDate.isAfter(now.plusYears(1))) {
            errors.add("Active date cannot be more than one year in the future");
        }
    }

    private void validateProductId(Long productId, List<String> errors) {
        if (productId <= 0) {
            errors.add("Product ID must be a positive number");
        }
    }

    private void validateBrandId(Long brandId, List<String> errors) {
        if (brandId <= 0) {
            errors.add("Brand ID must be a positive number");
        }
    }
}
