package com.challenge.tobacco.application.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDTOTest {

    @Test
    void testTransactionDTOConstructor() {
        // Arrange
        long expectedBundleId = 12345L;

        // Act
        TransactionDTO transactionDTO = new TransactionDTO(expectedBundleId);

        // Assert
        assertNotNull(transactionDTO);
        assertEquals(expectedBundleId, transactionDTO.bundleId());
    }
}
