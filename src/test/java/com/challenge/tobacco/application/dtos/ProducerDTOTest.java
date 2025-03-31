package com.challenge.tobacco.application.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProducerDTOTest {

    @Test
    void testProducerDTOConstructor() {
        // Arrange
        String expectedName = "John Doe";
        String expectedCpf = "12345678901";
        String expectedCep = "12345-678";

        // Act
        ProducerDTO producerDTO = new ProducerDTO(expectedName, expectedCpf, expectedCep);

        // Assert
        assertNotNull(producerDTO);
        assertEquals(expectedName, producerDTO.name());
        assertEquals(expectedCpf, producerDTO.cpf());
        assertEquals(expectedCep, producerDTO.cep());
    }
}
