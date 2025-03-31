package com.challenge.tobacco.application.dtos;

import com.challenge.tobacco.application.enums.ResponseStatus;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void testResponseConstructor() {
        // Arrange
        ResponseStatus expectedStatus = ResponseStatus.success;
        String expectedMessage = "Operation successful";
        Map<String, Object> expectedData = Map.of("key1", "value1", "key2", "value2");

        // Act
        Response response = new Response(expectedStatus, expectedMessage, expectedData);

        // Assert
        assertNotNull(response);
        assertEquals(expectedStatus, response.status());
        assertEquals(expectedMessage, response.message());
        assertEquals(expectedData, response.data());
    }

    @Test
    void testResponseConstructorWithNullData() {
        // Arrange
        ResponseStatus expectedStatus = ResponseStatus.error;
        String expectedMessage = "An error occurred";

        // Act
        Response response = new Response(expectedStatus, expectedMessage, null);

        // Assert
        assertNotNull(response);
        assertEquals(expectedStatus, response.status());
        assertEquals(expectedMessage, response.message());
        assertNull(response.data());
    }
}
