package com.example.controller;

import com.example.exception.RecordNotFoundException;
import com.example.exception.ValidationException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleRecordNotFound_ShouldReturnNotFoundStatus() {
        // Given
        RecordNotFoundException exception = new RecordNotFoundException("User not found.");

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleRecordNotFound(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleValidationException_ShouldReturnBadRequestStatus() {
        // Given
        ValidationException exception = new ValidationException("Invalid email.");

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerErrorStatus() {
        // Given
        Exception exception = new Exception("Unexpected error");

        // When
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}