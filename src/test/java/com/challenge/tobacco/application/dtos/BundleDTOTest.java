package com.challenge.tobacco.application.dtos;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class BundleDTOTest {

    @Test
    void testBundleDTOConstructor() {
        // Arrange
        String expectedLabel = "Bundle A";
        Instant expectedBoughtAt = Instant.now();
        long expectedProducerID = 12345L;
        long expectedClassID = 67890L;
        Double expectedWeight = 1.5;

        // Act
        BundleDTO bundleDTO = new BundleDTO(expectedLabel, expectedBoughtAt, expectedProducerID, expectedClassID, expectedWeight);

        // Assert
        assertNotNull(bundleDTO);
        assertEquals(expectedLabel, bundleDTO.label());
        assertEquals(expectedBoughtAt, bundleDTO.boughtAt());
        assertEquals(expectedProducerID, bundleDTO.producerID());
        assertEquals(expectedClassID, bundleDTO.classID());
        assertEquals(expectedWeight, bundleDTO.weight());
    }
}
