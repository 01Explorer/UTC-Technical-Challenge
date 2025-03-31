package com.challenge.tobacco.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {

    @Test
    void testCustomExceptionConstructor() {
        // Arrange
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        String expectedMessage = "Custom exception occurred";

        // Act
        CustomException exception = new CustomException(expectedStatus, expectedMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }
}
