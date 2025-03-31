package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.dtos.TobaccoClassDTO;
import com.challenge.tobacco.application.services.TobaccoClassService;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TobaccoClassControllerTest {

    @Mock
    private TobaccoClassService tobaccoClassService;

    @InjectMocks
    private TobaccoClassController tobaccoClassController;

    private TobaccoClassDTO tobaccoClassDTO;
    private TobaccoClass tobaccoClass;

    @BeforeEach
    void setUp() {
        tobaccoClass = new TobaccoClass( "Virginia");
        tobaccoClassDTO = new TobaccoClassDTO("Virginia");
    }

    @Test
    void getClassById_shouldReturnTobaccoClass_whenClassExists() {
        // Arrange
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.of(tobaccoClass));

        // Act
        ResponseEntity<Response> response = tobaccoClassController.getClassById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tobaccoClass, ((Map) response.getBody().data()).get("class"));
    }

    @Test
    void getClassById_shouldReturnNotFound_whenClassDoesNotExist() {
        // Arrange
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = tobaccoClassController.getClassById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getClasses_shouldReturnListOfTobaccoClasses_whenClassesExist() {
        // Arrange
        List<TobaccoClass> tobaccoClassList = List.of(tobaccoClass);
        when(tobaccoClassService.findAll()).thenReturn(Optional.of(tobaccoClassList));

        // Act
        ResponseEntity<Response> response = tobaccoClassController.getClasses();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tobaccoClassList, ((Map) response.getBody().data()).get("classes"));
    }

    @Test
    void getClasses_shouldReturnNotFound_whenClassesDoNotExist() {
        // Arrange
        when(tobaccoClassService.findAll()).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = tobaccoClassController.getClasses();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createClass_shouldReturnCreated_whenClassIsCreated() {
        // Arrange
        when(tobaccoClassService.createClass(any(TobaccoClass.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = tobaccoClassController.createClass(tobaccoClassDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("classId"));
    }

    @Test
    void createClass_shouldReturnUnprocessableEntity_whenExceptionIsThrown() {
        // Arrange
        when(tobaccoClassService.createClass(any(TobaccoClass.class))).thenThrow(new RuntimeException("Test Exception"));

        // Act
        ResponseEntity<Response> response = tobaccoClassController.createClass(tobaccoClassDTO);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void updateClass_shouldReturnOk_whenClassIsUpdated() {
        // Arrange
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.of(tobaccoClass));
        when(tobaccoClassService.updateClass(any(TobaccoClass.class), any(TobaccoClass.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = tobaccoClassController.updateClass(1L, tobaccoClassDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("classId"));
    }

    @Test
    void updateClass_shouldReturnNotFound_whenClassDoesNotExist() {
        // Arrange
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = tobaccoClassController.updateClass(1L, tobaccoClassDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteClass_shouldReturnOk_whenClassIsDeleted() throws InvalidTobaccoClassException {
        // Arrange
        when(tobaccoClassService.deleteClass(1L)).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = tobaccoClassController.deleteClass(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("classId"));
    }

    @Test
    void deleteClass_shouldReturnNotFound_whenClassDoesNotExist() throws InvalidTobaccoClassException {
        // Arrange
        doThrow(new InvalidTobaccoClassException(HttpStatus.NOT_FOUND, "Class not found")).when(tobaccoClassService).deleteClass(1L);

        // Act
        ResponseEntity<Response> response = tobaccoClassController.deleteClass(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}