package com.challenge.tobacco.application.services;

import com.challenge.tobacco.application.repositories.TransactionRepository;
import com.challenge.tobacco.application.services.impl.TransactionServiceImpl;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private Bundle bundle;

    @Mock
    private Producer producer;

    @Mock
    private TobaccoClass tobaccoClass;

    private Transaction transaction;
    private Instant from;
    private Instant to;

    @BeforeEach
    void setUp() {
        from = Instant.now().minusSeconds(3600);
        to = Instant.now();

        when(bundle.getId()).thenReturn(1L);
        when(bundle.getWeight()).thenReturn(10.0);
        when(bundle.getBoughtAt()).thenReturn(Instant.now());
        when(bundle.getProducer()).thenReturn(producer);
        when(bundle.getClassField()).thenReturn(tobaccoClass);
        when(tobaccoClass.getId()).thenReturn(1L);
        when(producer.getId()).thenReturn(1L);

        transaction = new Transaction(bundle);
        transaction.setId(1L);
    }

    @Test
    void create_shouldReturnId_whenTransactionIsCreated() throws InvalidTransactionException {
        // Arrange
        when(transactionRepository.findByBundleId(bundle.getId())).thenReturn(Optional.empty());
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        long result = transactionService.create(transaction);

        // Assert
        assertEquals(transaction.getId(), result);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void create_shouldThrowException_whenBundleAlreadyBought() {
        // Arrange
        when(transactionRepository.findByBundleId(bundle.getId())).thenReturn(Optional.of(transaction));

        // Act & Assert
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> transactionService.create(transaction));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Bundle already bought", exception.getMessage());
        verify(transactionRepository, never()).save(transaction);
    }

    @Test
    void totalBoughtByProducer_shouldReturnTotalWeight() {
        // Arrange
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findByBundleProducerId(1L)).thenReturn(Optional.of(transactions));

        // Act
        Optional<Double> result = transactionService.totalBoughtByProducer(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(10.0, result.get());
    }

    @Test
    void totalBoughtByProducer_shouldReturnEmptyOptional_whenNoTransactions() {
        // Arrange
        when(transactionRepository.findByBundleProducerId(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Double> result = transactionService.totalBoughtByProducer(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void totalBoughtByClass_shouldReturnTotalWeight() {
        // Arrange
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findByBundleClassFieldId(1L)).thenReturn(Optional.of(transactions));

        // Act
        Optional<Double> result = transactionService.totalBoughtByClass(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(10.0, result.get());
    }

    @Test
    void totalBoughtByClass_shouldReturnEmptyOptional_whenNoTransactions() {
        // Arrange
        when(transactionRepository.findByBundleClassFieldId(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Double> result = transactionService.totalBoughtByClass(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void totalBought_shouldReturnTotalWeight() {
        // Arrange
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        Optional<Double> result = transactionService.totalBought();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(10.0, result.get());
    }

    @Test
    void totalBoughtBetween_shouldReturnTotalWeight() {
        // Arrange
        List<Transaction> transactions = List.of(transaction);
        when(transactionRepository.findByBundleBoughtAtBetween(from, to)).thenReturn(Optional.of(transactions));

        // Act
        Optional<Double> result = transactionService.totalBoughtBetween(from, to);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(10.0, result.get());
    }

    @Test
    void totalBoughtBetween_shouldReturnEmptyOptional_whenNoTransactions() {
        // Arrange
        when(transactionRepository.findByBundleBoughtAtBetween(from, to)).thenReturn(Optional.empty());

        // Act
        Optional<Double> result = transactionService.totalBoughtBetween(from, to);

        // Assert
        assertFalse(result.isPresent());
    }
}