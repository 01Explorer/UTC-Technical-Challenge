package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.dtos.TransactionDTO;
import com.challenge.tobacco.application.services.BundleService;
import com.challenge.tobacco.application.services.TransactionService;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.entities.Transaction;
import com.challenge.tobacco.domain.exceptions.InvalidTransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionsControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private BundleService bundleService;

    @InjectMocks
    private TransactionsController transactionsController;

    private TransactionDTO transactionDTO;
    private Bundle bundle;
    private Transaction transaction;
    private Instant from;
    private Instant to;

    @BeforeEach
    void setUp() {
        bundle = new Bundle( "Label", Instant.now(), new Producer(1L, "name", "cpf", null, Instant.now(), Instant.now()), new TobaccoClass("BO1"), 10.0);
        transaction = new Transaction( bundle);
        transactionDTO = new TransactionDTO(1L);
        from = Instant.now().minusSeconds(3600);
        to = Instant.now();
    }

    @Test
    void createTransaction_shouldReturnCreated_whenTransactionIsCreated() throws InvalidTransactionException {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.of(bundle));
        when(transactionService.create(any(Transaction.class))).thenReturn(1L);

        // Act
        ResponseEntity<Response> response = transactionsController.createTransaction(transactionDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, ((Map) response.getBody().data()).get("transactionId"));
    }

    @Test
    void createTransaction_shouldReturnNotFound_whenBundleNotFound() throws InvalidTransactionException {
        // Arrange
        when(bundleService.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = transactionsController.createTransaction(transactionDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTotalBoughtByProducer_shouldReturnTotalBought_whenProducerIdIsProvided() {
        // Arrange
        when(transactionService.totalBoughtByProducer(1L)).thenReturn(Optional.of(20.0));

        // Act
        ResponseEntity<Response> response = transactionsController.getTotalBoughtByProducer(1L, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(20.0, ((Map) response.getBody().data()).get("totalBought"));
    }

    @Test
    void getTotalBoughtByClass_shouldReturnTotalBought_whenClassIdIsProvided() {
        // Arrange
        when(transactionService.totalBoughtByClass(1L)).thenReturn(Optional.of(30.0));

        // Act
        ResponseEntity<Response> response = transactionsController.getTotalBoughtByProducer(null, 1L, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(30.0, ((Map) response.getBody().data()).get("totalBought"));
    }

    @Test
    void getTotalBoughtBetween_shouldReturnTotalBought_whenFromAndToAreProvided() {
        // Arrange
        when(transactionService.totalBoughtBetween(from, to)).thenReturn(Optional.of(40.0));

        // Act
        ResponseEntity<Response> response = transactionsController.getTotalBoughtByProducer(null, null, from, to);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(40.0, ((Map) response.getBody().data()).get("totalBought"));
    }

    @Test
    void getTotalBought_shouldReturnTotalBought_whenNoParamsAreProvided() {
        // Arrange
        when(transactionService.totalBought()).thenReturn(Optional.of(50.0));

        // Act
        ResponseEntity<Response> response = transactionsController.getTotalBoughtByProducer(null, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50.0, ((Map) response.getBody().data()).get("totalBought"));
    }

    @Test
    void getTotalBought_shouldReturnZero_whenNoTransactionsFound() {
        // Arrange
        when(transactionService.totalBought()).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Response> response = transactionsController.getTotalBoughtByProducer(null, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, ((Map) response.getBody().data()).get("totalBought"));
    }
}