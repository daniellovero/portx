package com.portx.dlovero.paymentsrestapi.domain.validations.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CustomPaymentAlreadyExists.class)
    public ResponseEntity<Object> handleError(CustomPaymentAlreadyExists ex, WebRequest request) {
        String bodyResponse = ex.getMessage();
        // Because of requirement, otherwise it could be conflict (409) instead of 500.
        return handleExceptionInternal(ex, bodyResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({CustomInvalidField.class, CustomInvalidUUIDVersion.class})
    public ResponseEntity<Object> handleError(CustomExceptions ex, WebRequest request) {
        String bodyResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex, WebRequest request) {
        String bodyResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
