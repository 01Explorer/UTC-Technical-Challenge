package com.challenge.tobacco.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class AddressExceptionTest {

    @Test
    void testAddressExceptionConstructor() {
        // Arrange
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        String expectedMessage = "Address error occurred";

        // Act
        AddressException exception = new AddressException(expectedStatus, expectedMessage);

        // Assert
        assertNotNull(exception); // Ensure the exception is not null
        assertEquals(expectedStatus, exception.getStatus()); // Assuming CustomException has a getStatus() method
        assertEquals(expectedMessage, exception.getMessage()); // Assuming CustomException stores the message
    }
}
