package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.ProducerDTO;
import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.services.ProducerService;
import com.challenge.tobacco.domain.entities.Address;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.exceptions.AddressException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProducerControllerTest {

    @Mock
    private ProducerService producerService;

    @InjectMocks
    private ProducerController producerController;

    private ProducerDTO producerDTO;
    private Producer producer;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("12345678", "street", "city", "state", "street");
        producer = new Producer(1L, "John Doe", "12345678900", address, Instant.now(), Instant.now());
        producerDTO = new ProducerDTO("John Doe", "12345678900", "12345678");
    }

    @Test
    void getProducer_shouldReturnProducer_whenProducerExists() {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));

        // Act
        ResponseEntity<Response> response = producerController.getProducer(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producer, ((Map) response.getBody().data()).get("producer"));
    }

    @Test
    void getProducer_shouldReturnNotFound_whenProducerDoesNotExist() {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = producerController.getProducer(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getProducers_shouldReturnListOfProducers_whenProducersExist() {
        // Arrange
        List<Producer> producerList = List.of(producer);
        when(producerService.getAllProducers()).thenReturn(Optional.of(producerList));

        // Act
        ResponseEntity<Response> response = producerController.getProducers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producerList, ((Map) response.getBody().data()).get("producers"));
    }

    @Test
    void getProducers_shouldReturnNotFound_whenProducersDoNotExist() {
        // Arrange
        when(producerService.getAllProducers()).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = producerController.getProducers();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createProducer_shouldReturnCreated_whenProducerIsCreated() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.getAddress("12345678")).thenReturn(address);
        when(producerService.createProducer(any(Producer.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = producerController.createProducer(producerDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("producerId"));
    }

    @Test
    void createProducer_shouldReturnUnprocessableEntity_whenInvalidProducerExceptionIsThrown() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.getAddress("12345678")).thenReturn(address);
        when(producerService.createProducer(any(Producer.class))).thenThrow(new InvalidProducerException(HttpStatus.CONFLICT, "Producer already exists"));

        // Act
        ResponseEntity<Response> response = producerController.createProducer(producerDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void createProducer_shouldReturnUnprocessableEntity_whenAddressExceptionIsThrown() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.getAddress("12345678")).thenThrow(new AddressException(HttpStatus.BAD_REQUEST, "Invalid CEP"));

        // Act
        ResponseEntity<Response> response = producerController.createProducer(producerDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateProducer_shouldReturnOk_whenProducerIsUpdated() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(producerService.updateProducer(any(Producer.class), any(Producer.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = producerController.updateProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("producerId"));
    }

    @Test
    void updateProducer_shouldReturnNotFound_whenProducerDoesNotExist() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = producerController.updateProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProducer_shouldReturnUnprocessableEntity_whenInvalidProducerExceptionIsThrownOnUpdate() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(producerService.updateProducer(any(Producer.class), any(Producer.class))).thenThrow(new InvalidProducerException(HttpStatus.CONFLICT, "Producer already exists"));

        // Act
        ResponseEntity<Response> response = producerController.updateProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void updateProducer_shouldReturnUnprocessableEntity_whenAddressExceptionIsThrownOnUpdate() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(producerService.updateProducer(any(Producer.class), any(Producer.class))).thenThrow(new AddressException(HttpStatus.BAD_REQUEST, "Invalid CEP"));

        // Act
        ResponseEntity<Response> response = producerController.updateProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void patchProducer_shouldReturnOk_whenProducerIsPatched() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(producerService.patchProducer(any(Producer.class), any(Producer.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = producerController.patchProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("producerId"));
    }

    @Test
    void patchProducer_shouldReturnNotFound_whenProducerDoesNotExistOnPatch() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = producerController.patchProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void patchProducer_shouldReturnUnprocessableEntity_whenInvalidProducerExceptionIsThrownOnPatch() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(producerService.patchProducer(any(Producer.class), any(Producer.class))).thenThrow(new InvalidProducerException(HttpStatus.CONFLICT, "Producer already exists"));

        // Act
        ResponseEntity<Response> response = producerController.patchProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void patchProducer_shouldReturnUnprocessableEntity_whenAddressExceptionIsThrownOnPatch() throws InvalidProducerException, AddressException {
        // Arrange
        when(producerService.findById(1L)).thenReturn(Optional.of(producer));
        when(producerService.patchProducer(any(Producer.class), any(Producer.class))).thenThrow(new AddressException(HttpStatus.BAD_REQUEST, "Invalid CEP"));

        // Act
        ResponseEntity<Response> response = producerController.patchProducer(1L, producerDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteProducer_shouldReturnOk_whenProducerIsDeleted() throws InvalidProducerException {
        // Arrange
        when(producerService.deleteProducer(1L)).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = producerController.deleteProducer(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("producerId"));
    }

    @Test
    void deleteProducer_shouldReturnNotFound_whenProducerDoesNotExistOnDelete() throws InvalidProducerException {
        // Arrange
        when(producerService.deleteProducer(1L)).thenThrow(new InvalidProducerException(HttpStatus.NOT_FOUND, "Producer not found"));

        // Act
        ResponseEntity<Response> response = producerController.deleteProducer(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}