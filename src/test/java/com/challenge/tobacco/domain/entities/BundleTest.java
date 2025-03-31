package com.challenge.tobacco.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BundleTest {

    @Mock
    private Producer producer;

    @Mock
    private TobaccoClass tobaccoClass;

    private String validLabel;
    private Instant validBoughtAt;
    private Double validWeight;

    @BeforeEach
    void setUp() {
        validLabel = "Test Bundle";
        validBoughtAt = Instant.now().minusSeconds(60);
        validWeight = 10.5;
    }

    @Test
    void constructor_WithValidParameters_ShouldCreateBundle() {
        // Act
        Bundle bundle = new Bundle(validLabel, validBoughtAt, producer, tobaccoClass, validWeight);

        // Assert
        assertEquals(validLabel, bundle.getLabel());
        assertEquals(validBoughtAt, bundle.getBoughtAt());
        assertSame(producer, bundle.getProducer());
        assertSame(tobaccoClass, bundle.getClassField());
        assertEquals(validWeight, bundle.getWeight());
    }

    @Test
    void constructor_WithFutureBoughtAt_ShouldThrowException() {
        // Arrange
        Instant futureBoughtAt = Instant.now().plusSeconds(30);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bundle(validLabel, futureBoughtAt, producer, tobaccoClass, validWeight)
        );

        assertTrue(exception.getMessage().contains("Bought at can't be after now"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void constructor_WithEmptyLabel_ShouldThrowException(String emptyLabel) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bundle(emptyLabel, validBoughtAt, producer, tobaccoClass, validWeight)
        );

        assertTrue(exception.getMessage().contains("Label can't be empty"));
    }

    @Test
    void constructor_WithNullLabel_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bundle(null, validBoughtAt, producer, tobaccoClass, validWeight)
        );

        assertTrue(exception.getMessage().contains("Label can't be empty"));
    }

    @Test
    void constructor_WithNullProducer_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bundle(validLabel, validBoughtAt, null, tobaccoClass, validWeight)
        );

        assertTrue(exception.getMessage().contains("Producer can't be null"));
    }

    @Test
    void constructor_WithNullClass_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bundle(validLabel, validBoughtAt, producer, null, validWeight)
        );

        assertTrue(exception.getMessage().contains("Class can't be null"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -10.5})
    void constructor_WithInvalidWeight_ShouldThrowException(Double invalidWeight) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Bundle(validLabel, validBoughtAt, producer, tobaccoClass, invalidWeight)
        );

        assertTrue(exception.getMessage().contains("Weight needs to be greater than 0"));
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Bundle bundle = new Bundle(validLabel, validBoughtAt, producer, tobaccoClass, validWeight);
        Long newId = 2L;
        String newLabel = "New Label";
        Instant newBoughtAt = Instant.now().minusSeconds(120);

        Double newWeight = 20.5;

        // Act
        bundle.setId(newId);
        bundle.setLabel(newLabel);
        bundle.setBoughtAt(newBoughtAt);
        bundle.setProducer(producer);
        bundle.setClassField(tobaccoClass);
        bundle.setWeight(newWeight);

        // Assert
        assertEquals(newId, bundle.getId());
        assertEquals(newLabel, bundle.getLabel());
        assertEquals(newBoughtAt, bundle.getBoughtAt());
        assertSame(producer, bundle.getProducer());
        assertSame(tobaccoClass, bundle.getClassField());
        assertEquals(newWeight, bundle.getWeight());
    }

    @Test
    void protectedConstructor_ShouldExist() {
        assertDoesNotThrow(() -> Bundle.class.getDeclaredConstructor().setAccessible(true));
    }
}