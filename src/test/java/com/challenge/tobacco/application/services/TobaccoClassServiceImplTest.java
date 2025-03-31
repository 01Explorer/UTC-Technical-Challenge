package com.challenge.tobacco.application.services;

import com.challenge.tobacco.application.repositories.TobaccoClassRepository;
import com.challenge.tobacco.application.services.impl.TobaccoClassServiceImpl;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TobaccoClassServiceImplTest {

    @Mock
    private TobaccoClassRepository tobaccoClassRepository;

    @InjectMocks
    private TobaccoClassServiceImpl tobaccoClassService;

    private TobaccoClass tobaccoClass;

    @BeforeEach
    void setUp() {
        tobaccoClass = new TobaccoClass( "Virginia");
        tobaccoClass.setId( 1L);
    }

    @Test
    void createClass_shouldReturnId_whenClassIsCreated() {
        // Arrange
        when(tobaccoClassRepository.save(tobaccoClass)).thenReturn(tobaccoClass);

        // Act
        long result = tobaccoClassService.createClass(tobaccoClass);

        // Assert
        assertEquals(tobaccoClass.getId(), result);
        verify(tobaccoClassRepository, times(1)).save(tobaccoClass);
    }

    @Test
    void findById_shouldReturnTobaccoClass_whenClassExists() {
        // Arrange
        when(tobaccoClassRepository.findById(tobaccoClass.getId())).thenReturn(Optional.of(tobaccoClass));

        // Act
        Optional<TobaccoClass> result = tobaccoClassService.findById(tobaccoClass.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tobaccoClass, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenClassDoesNotExist() {
        // Arrange
        when(tobaccoClassRepository.findById(tobaccoClass.getId())).thenReturn(Optional.empty());

        // Act
        Optional<TobaccoClass> result = tobaccoClassService.findById(tobaccoClass.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_shouldReturnListOfTobaccoClasses() {
        // Arrange
        List<TobaccoClass> tobaccoClasses = List.of(tobaccoClass);
        when(tobaccoClassRepository.findAll()).thenReturn(tobaccoClasses);

        // Act
        Optional<List<TobaccoClass>> result = tobaccoClassService.findAll();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tobaccoClasses, result.get());
    }

    @Test
    void updateClass_shouldReturnId_whenClassIsUpdated() {
        // Arrange
        TobaccoClass newClass = new TobaccoClass("Burley");
        newClass.setId(1L);
        TobaccoClass oldClass = new TobaccoClass( "Virginia");
        oldClass.setId(1L);
        when(tobaccoClassRepository.save(oldClass)).thenReturn(oldClass);

        // Act
        long result = tobaccoClassService.updateClass(newClass, oldClass);

        // Assert
        assertEquals(oldClass.getId(), result);
        assertEquals(newClass.getDescription(), oldClass.getDescription());
        verify(tobaccoClassRepository, times(1)).save(oldClass);
    }

    @Test
    void deleteClass_shouldReturnId_whenClassIsDeleted() throws InvalidTobaccoClassException {
        // Arrange
        when(tobaccoClassRepository.findById(tobaccoClass.getId())).thenReturn(Optional.of(tobaccoClass));
        doNothing().when(tobaccoClassRepository).deleteById(tobaccoClass.getId());

        // Act
        long result = tobaccoClassService.deleteClass(tobaccoClass.getId());

        // Assert
        assertEquals(tobaccoClass.getId(), result);
        verify(tobaccoClassRepository, times(1)).deleteById(tobaccoClass.getId());
    }

    @Test
    void deleteClass_shouldThrowException_whenClassDoesNotExist() {
        // Arrange
        when(tobaccoClassRepository.findById(tobaccoClass.getId())).thenReturn(Optional.empty());

        // Act & Assert
        InvalidTobaccoClassException exception = assertThrows(InvalidTobaccoClassException.class, () -> tobaccoClassService.deleteClass(tobaccoClass.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Tobacco Class with ID: %s, not found", tobaccoClass.getId()), exception.getMessage());
        verify(tobaccoClassRepository, never()).deleteById(anyLong());
    }
}