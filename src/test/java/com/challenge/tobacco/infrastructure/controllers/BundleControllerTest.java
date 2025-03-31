package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.BundleDTO;
import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.services.BundleService;
import com.challenge.tobacco.application.services.ProducerService;
import com.challenge.tobacco.application.services.TobaccoClassService;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidBundleException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BundleControllerTest {

    @Mock
    private BundleService bundleService;

    @Mock
    private ProducerService producerService;

    @Mock
    private TobaccoClassService tobaccoClassService;

    @InjectMocks
    private BundleController bundleController;

    private BundleDTO bundleDTO;
    private Bundle bundle;
    private Producer producer;
    private TobaccoClass tobaccoClass;

    @BeforeEach
    void setUp() {
        producer = new Producer(1L, "Producer Name", "12345678900", null, Instant.now(), Instant.now());
        tobaccoClass = new TobaccoClass( "Tobacco Class Description");
        bundle = new Bundle( "Label", Instant.now(), producer, tobaccoClass, 10.0);
        bundleDTO = new BundleDTO("Label", Instant.now(), 1L, 1L, 10.0);
    }

    @Test
    void getBundleById_shouldReturnBundle_whenBundleExists() {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.of(bundle));

        // Act
        ResponseEntity<Response> response = bundleController.getBundleById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bundle, ((Map) response.getBody().data()).get("bundle"));
    }

    @Test
    void getBundleById_shouldReturnNotFound_whenBundleDoesNotExist() {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.getBundleById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getBundles_shouldReturnListOfBundles_whenBundlesExist() {
        // Arrange
        List<Bundle> bundleList = List.of(bundle);
        when(bundleService.findAll()).thenReturn(Optional.of(bundleList));

        // Act
        ResponseEntity<Response> response = bundleController.getBundles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bundleList, ((Map) response.getBody().data()).get("bundles"));
    }

    @Test
    void getBundles_shouldReturnNotFound_whenBundlesDoNotExist() {
        // Arrange
        when(bundleService.findAll()).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.getBundles();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createBundle_shouldReturnCreated_whenBundleIsCreated() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.of(tobaccoClass));
        when(bundleService.createBundle(any(Bundle.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = bundleController.createBundle(bundleDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("bundleId"));
    }

    @Test
    void createBundle_shouldReturnUnprocessableEntity_whenProducerNotFound() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.createBundle(bundleDTO);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void createBundle_shouldReturnUnprocessableEntity_whenTobaccoClassNotFound() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.createBundle(bundleDTO);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void updateBundle_shouldReturnOk_whenBundleIsUpdated() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.of(bundle));
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.of(tobaccoClass));
        when(bundleService.updateBundle(any(Bundle.class), any(Bundle.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = bundleController.updateBundle(1L, bundleDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("bundleId"));
    }

    @Test
    void updateBundle_shouldReturnNotFound_whenBundleDoesNotExist() {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.updateBundle(1L, bundleDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateBundle_shouldReturnUnprocessableEntity_whenProducerNotFound() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.of(bundle));
        when(producerService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.updateBundle(1L, bundleDTO);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void updateBundle_shouldReturnUnprocessableEntity_whenTobaccoClassNotFound() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.of(bundle));
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.updateBundle(1L, bundleDTO);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void deleteBundle_shouldReturnOk_whenBundleIsDeleted() throws InvalidBundleException {
        // Arrange
        when(bundleService.deleteBundle(1L)).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = bundleController.deleteBundle(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("bundleId"));
    }

    @Test
    void deleteBundle_shouldReturnNotFound_whenBundleDoesNotExist() throws InvalidBundleException {
        // Arrange
        doThrow(new InvalidBundleException(HttpStatus.NOT_FOUND, "Bundle not found")).when(bundleService).deleteBundle(1L);

        // Act
        ResponseEntity<Response> response = bundleController.deleteBundle(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void patchBundle_shouldReturnOk_whenBundleIsPatched() throws InvalidProducerException, InvalidTobaccoClassException, InvalidBundleException {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.of(bundle));
        when(bundleService.patchBundle(any(Bundle.class), any(Bundle.class))).thenReturn(1L);
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(tobaccoClassService.findById(1L)).thenReturn(Optional.of(tobaccoClass));

        // Act
        ResponseEntity<Response> response = bundleController.patchBundle(1L, bundleDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("bundleId"));
    }

    @Test
    void patchBundle_shouldReturnNotFound_whenBundleDoesNotExist() {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = bundleController.patchBundle(1L, bundleDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}