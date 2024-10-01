package com.example.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionsTest {

    @Test
    void testRecordNotFoundExceptionMessage() {
        String errorMessage = "User not found with ID: 123";
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            throw new RecordNotFoundException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testValidationExceptionMessage() {
        String errorMessage = "Invalid data provided for: email";
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            throw new ValidationException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}