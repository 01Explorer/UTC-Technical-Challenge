package com.challenge.tobacco.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class InvalidBundleExceptionTest {

    @Test
    void testInvalidBundleExceptionConstructor() {
        // Arrange
        HttpStatus expectedStatus = HttpStatus.FORBIDDEN;
        String expectedMessage = "Invalid bundle exception occurred";

        // Act
        InvalidBundleException exception = new InvalidBundleException(expectedStatus, expectedMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }
}
