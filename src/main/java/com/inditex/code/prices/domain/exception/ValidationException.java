package com.inditex.code.prices.domain.exception;

import java.util.List;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
