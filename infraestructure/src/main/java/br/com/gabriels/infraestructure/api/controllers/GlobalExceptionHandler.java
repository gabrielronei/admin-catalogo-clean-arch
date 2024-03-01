package br.com.gabriels.infraestructure.api.controllers;

import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(final NotFoundException domainException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(domainException.getMessage()));
    }

    @ExceptionHandler(value = {DomainException.class})
    public ResponseEntity<?> handleDomainException(final DomainException domainException) {
        return ResponseEntity.unprocessableEntity().body(new ApiError(domainException.getMessage()));
    }

    record ApiError(String message) {
    }
}
