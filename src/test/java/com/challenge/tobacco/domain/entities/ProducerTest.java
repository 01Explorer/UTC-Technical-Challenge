package com.challenge.tobacco.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class ProducerTest {

    @Mock
    private Address mockAddress;

    private Long id;
    private String name;
    private String cpf;
    private Instant createdAt;
    private Instant updatedAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "Test Producer";
        cpf = "12345678901";
        createdAt = Instant.parse("2023-01-01T10:00:00Z");
        updatedAt = Instant.parse("2023-01-02T10:00:00Z");
    }

    @Test
    void constructor_WithAllParameters_ShouldCreateProducer() {
        // Act
        Producer producer = new Producer(id, name, cpf, mockAddress, createdAt, updatedAt);

        // Assert
        assertEquals(id, producer.getId());
        assertEquals(name, producer.getName());
        assertEquals(cpf, producer.getCpf());
        assertSame(mockAddress, producer.getAddress());
        assertEquals(createdAt, producer.getCreatedAt());
        assertEquals(updatedAt, producer.getUpdatedAt());
    }

    @Test
    void defaultConstructor_ShouldCreateProducer() {
        // Act
        Producer producer = new Producer();

        // Assert
        assertNull(producer.getId());
        assertNull(producer.getName());
        assertNull(producer.getCpf());
        assertNull(producer.getAddress());
        assertNull(producer.getCreatedAt());
        assertNull(producer.getUpdatedAt());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        Producer producer = new Producer();

        // Act
        producer.setName(name);
        producer.setCpf(cpf);
        producer.setAddress(mockAddress);
        producer.setCreatedAt(createdAt);
        producer.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(name, producer.getName());
        assertEquals(cpf, producer.getCpf());
        assertSame(mockAddress, producer.getAddress());
        assertEquals(createdAt, producer.getCreatedAt());
        assertEquals(updatedAt, producer.getUpdatedAt());
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        // Arrange
        Producer producer1 = new Producer(id, "Producer 1", "11111111111", mockAddress, createdAt, updatedAt);
        Producer producer2 = new Producer(id, "Producer 2", "22222222222", mockAddress, createdAt, updatedAt);

        // Act & Assert
        assertEquals(producer1, producer2);
        assertEquals(producer1.hashCode(), producer2.hashCode());
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        // Arrange
        Producer producer1 = new Producer(1L, name, cpf, mockAddress, createdAt, updatedAt);
        Producer producer2 = new Producer(2L, name, cpf, mockAddress, createdAt, updatedAt);

        // Act & Assert
        assertNotEquals(producer1, producer2);
        assertNotEquals(producer1.hashCode(), producer2.hashCode());
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        // Arrange
        Producer producer = new Producer(id, name, cpf, mockAddress, createdAt, updatedAt);

        // Act & Assert
        assertNotEquals(producer, null);
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        // Arrange
        Producer producer = new Producer(id, name, cpf, mockAddress, createdAt, updatedAt);
        Object otherObject = new Object();

        // Act & Assert
        assertNotEquals(producer, otherObject);
    }

    @Test
    void equals_WithSameInstance_ShouldReturnTrue() {
        // Arrange
        Producer producer = new Producer(id, name, cpf, mockAddress, createdAt, updatedAt);

        // Act & Assert
        assertEquals(producer, producer);
    }

    @Test
    void hashCode_WithNullId_ShouldNotThrowException() {
        // Arrange
        Producer producer = new Producer(null, name, cpf, mockAddress, createdAt, updatedAt);

        // Act & Assert
        assertDoesNotThrow(producer::hashCode);
    }
}