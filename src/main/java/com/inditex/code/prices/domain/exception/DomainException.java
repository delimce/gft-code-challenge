package com.inditex.code.prices.domain.exception;

public abstract class DomainException extends Throwable {

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

}