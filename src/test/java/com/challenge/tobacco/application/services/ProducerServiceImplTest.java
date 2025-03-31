package com.challenge.tobacco.application.services;

import com.challenge.tobacco.application.repositories.ProducerRepository;
import com.challenge.tobacco.application.services.impl.ProducerServiceImpl;
import com.challenge.tobacco.domain.entities.Address;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.exceptions.AddressException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProducerServiceImplTest {

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private ProducerServiceImpl producerService;

    private Producer producer;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("12345678", "street", "city", "state", "street");
        producer = new Producer(1L, "John Doe", "12345678900", address, Instant.now(), Instant.now());

        when(restClientBuilder.build()).thenReturn(restClient);

        producerService = new ProducerServiceImpl(producerRepository, restClientBuilder);
    }

    @Test
    void createProducer_shouldReturnId_whenProducerIsCreated() throws InvalidProducerException {
        // Arrange
        when(producerRepository.findByCpf(producer.getCpf())).thenReturn(Optional.empty());
        when(producerRepository.save(producer)).thenReturn(producer);

        // Act
        long result = producerService.createProducer(producer);

        // Assert
        assertEquals(producer.getId(), result);
        verify(producerRepository, times(1)).save(producer);
    }

    @Test
    void createProducer_shouldThrowException_whenCpfAlreadyExists() {
        // Arrange
        when(producerRepository.findByCpf(producer.getCpf())).thenReturn(Optional.of(producer));

        // Act & Assert
        InvalidProducerException exception = assertThrows(InvalidProducerException.class, () -> producerService.createProducer(producer));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(String.format("Producer with CPF: %s already exists", producer.getCpf()), exception.getMessage());
        verify(producerRepository, never()).save(producer);
    }

    @Test
    void findById_shouldReturnProducer_whenProducerExists() {
        // Arrange
        when(producerRepository.findById(1L)).thenReturn(Optional.of(producer));

        // Act
        Optional<Producer> result = producerService.findById(producer.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(producer, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenProducerDoesNotExist() {
        // Arrange
        when(producerRepository.findById(producer.getId())).thenReturn(Optional.empty());

        // Act
        Optional<Producer> result = producerService.findById(producer.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void getAllProducers_shouldReturnListOfProducers() {
        // Arrange
        List<Producer> producers = List.of(producer);
        when(producerRepository.findAll()).thenReturn(producers);

        // Act
        Optional<List<Producer>> result = producerService.getAllProducers();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(producers, result.get());
    }

    @Test
    void updateProducer_shouldReturnId_whenProducerIsUpdated() throws InvalidProducerException, AddressException {
        // Arrange
        Producer newProducer = new Producer(1L, "Jane Doe", "98765432100", new Address("87654321", "new street", "new city","newState", "street"), Instant.now(), Instant.now());
        when(producerRepository.findByCpf(newProducer.getCpf())).thenReturn(Optional.empty());
        when(producerRepository.save(producer)).thenReturn(producer);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Address.class)).thenReturn(newProducer.getAddress());

        // Act
        long result = producerService.updateProducer(newProducer, producer);

        // Assert
        assertEquals(producer.getId(), result);
        verify(producerRepository, times(1)).save(producer);
    }

    @Test
    void updateProducer_shouldThrowException_whenCpfAlreadyExistsForNewProducer() {
        // Arrange
        Producer newProducer = new Producer(1L, "Jane Doe", "98765432100", address, Instant.now(), Instant.now());
        when(producerRepository.findByCpf(newProducer.getCpf())).thenReturn(Optional.of(newProducer));

        // Act & Assert
        InvalidProducerException exception = assertThrows(InvalidProducerException.class, () -> producerService.updateProducer(newProducer, producer));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(String.format("Producer with CPF: %s already exists", newProducer.getCpf()), exception.getMessage());
        verify(producerRepository, never()).save(any(Producer.class));
    }

    @Test
    void deleteProducer_shouldReturnId_whenProducerIsDeleted() throws InvalidProducerException {
        // Arrange
        when(producerRepository.findById(1L)).thenReturn(Optional.of(producer));
        doNothing().when(producerRepository).delete(producer);

        // Act
        long result = producerService.deleteProducer(producer.getId());

        // Assert
        assertEquals(producer.getId(), result);
        verify(producerRepository, times(1)).delete(producer);
    }

    @Test
    void deleteProducer_shouldThrowException_whenProducerDoesNotExist() {
        // Arrange
        when(producerRepository.findById(producer.getId())).thenReturn(Optional.empty());

        // Act & Assert
        InvalidProducerException exception = assertThrows(InvalidProducerException.class, () -> producerService.deleteProducer(producer.getId()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Producer with ID: %s not found", producer.getId()), exception.getMessage());
        verify(producerRepository, never()).delete(any(Producer.class));
    }

    @Test
    void getAddress_shouldReturnAddress_whenCepIsValid() throws AddressException {
        // Arrange
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Address.class)).thenReturn(address);

        // Act
        Address result = producerService.getAddress("12345678");

        // Assert
        assertEquals(address, result);
    }

    @Test
    void getAddress_shouldThrowException_whenAddressServiceFails() {
        // Arrange
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        // Act & Assert
        AddressException exception = assertThrows(AddressException.class, () -> producerService.getAddress("12345678"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void patchProducer_shouldUpdateFieldsAndReturnId() throws InvalidProducerException, AddressException {
        // Arrange
        Producer newProducer = new Producer(1L, null, null, new Address(null,null,null,null, null), Instant.now(), Instant.now());
        Producer patchedProducer = new Producer(1L, producer.getName(), producer.getCpf(), producer.getAddress(), Instant.now(), Instant.now());
        when(producerRepository.findByCpf(patchedProducer.getCpf())).thenReturn(Optional.empty());
        when(producerRepository.save(patchedProducer)).thenReturn(patchedProducer);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Address.class)).thenReturn(producer.getAddress());

        // Act
        long result = producerService.patchProducer(newProducer, producer);

        // Assert
        assertEquals(patchedProducer.getId(), result);
        verify(producerRepository, times(1)).save(patchedProducer);
    }
}