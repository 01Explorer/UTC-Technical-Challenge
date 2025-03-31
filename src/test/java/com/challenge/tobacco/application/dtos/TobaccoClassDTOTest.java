package com.challenge.tobacco.application.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TobaccoClassDTOTest {

    @Test
    void testTobaccoClassDTOConstructor() {
        // Arrange
        String expectedDescription = "Premium Tobacco";

        // Act
        TobaccoClassDTO tobaccoClassDTO = new TobaccoClassDTO(expectedDescription);

        // Assert
        assertNotNull(tobaccoClassDTO);
        assertEquals(expectedDescription, tobaccoClassDTO.description());
    }
}
