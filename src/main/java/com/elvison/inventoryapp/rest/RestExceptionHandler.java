package com.elvison.inventoryapp.rest;

import com.elvison.inventoryapp.exception.InternalServerException;
import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.model.rest.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiError> handleInternalServerException(InternalServerException e) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
        return build(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, Throwable t) {
        return ResponseEntity.status(status).body(ApiError.of(t.getMessage()));
    }
}
