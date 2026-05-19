package com.beknazarovmiras.booking.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class BeknazarovMirasGlobalExceptionHandler {

    @ExceptionHandler(BeknazarovMirasNotFoundException.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleNotFound(BeknazarovMirasNotFoundException ex) {
        log.error("Not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
    }

    @ExceptionHandler(BeknazarovMirasBadRequestException.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleBadRequest(BeknazarovMirasBadRequestException ex) {
        log.error("Bad request: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), null);
    }

    @ExceptionHandler(BeknazarovMirasUnauthorizedException.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleUnauthorized(BeknazarovMirasUnauthorizedException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid email or password", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Forbidden", "Access denied", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        log.warn("Validation failed: {}", errors);
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", "Request validation error", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BeknazarovMirasErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred", null);
    }

    private ResponseEntity<BeknazarovMirasErrorResponse> build(
            HttpStatus status, String error, String message, Map<String, String> fieldErrors) {
        return ResponseEntity.status(status).body(BeknazarovMirasErrorResponse.builder()
                .status(status.value()).error(error).message(message)
                .timestamp(LocalDateTime.now()).fieldErrors(fieldErrors).build());
    }
}
