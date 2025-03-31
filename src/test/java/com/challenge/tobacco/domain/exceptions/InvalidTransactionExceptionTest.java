package com.challenge.tobacco.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InvalidTransactionExceptionTest {

    @Test
    void testInvalidTransactionExceptionConstructor() {
        // Arrange
        HttpStatus expectedStatus = HttpStatus.FORBIDDEN;
        String expectedMessage = "Invalid bundle exception occurred";

        // Act
        InvalidTransactionException exception = new InvalidTransactionException(expectedStatus, expectedMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }
}
