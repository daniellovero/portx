package com.portx.dlovero.paymentsrestapi.domain.validations.exceptions;

// In order to filter it with Spring Exception Handler
public abstract class CustomExceptions extends RuntimeException {
    public CustomExceptions(String message) {
        super(message);
    }

    public CustomExceptions() {
    }

}
