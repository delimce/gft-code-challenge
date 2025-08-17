package com.inditex.code.prices.infrastructure.in.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.inditex.code.prices.domain.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     *
     * @param ex the exception
     * @return a response entity with error details
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("errors", ex.getErrors());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles method argument type mismatch exceptions.
     *
     * @param ex the exception
     * @return a response entity with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");

        String parameterName = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        String expectedTypeName = requiredType != null
                ? getTargetTypeName(requiredType.getSimpleName())
                : "expected type";
        String errorMessage = String.format("Parameter '%s' should be a valid %s",
                parameterName,
                expectedTypeName);

        body.put("message", errorMessage);
        body.put("errors", List.of(errorMessage));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gets a user-friendly name for a type.
     *
     * @param typeName the type name
     * @return a user-friendly name for the type
     */
    private String getTargetTypeName(String typeName) {
        return switch (typeName) {
            case "Long" -> "number";
            case "LocalDateTime" -> "date-time (ISO format: yyyy-MM-ddTHH:mm:ss)";
            default -> typeName.toLowerCase();
        };
    }
}
