package com.challenge.tobacco.application.services.impl;

import com.challenge.tobacco.application.repositories.TransactionRepository;
import com.challenge.tobacco.application.services.TransactionService;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.entities.Transaction;
import com.challenge.tobacco.domain.exceptions.InvalidTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public long create(Transaction transaction) throws InvalidTransactionException {
        Optional<Transaction> maybeTransaction = transactionRepository.findByBundleId(transaction.getBundle().getId());
        if (maybeTransaction.isPresent()) {
            throw new InvalidTransactionException(HttpStatus.BAD_REQUEST,"Bundle already bought");
        }
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public Optional<Double> totalBoughtByProducer(long producerId) {
        Optional<List<Transaction>> transactions = transactionRepository.findByBundleProducerId(producerId);
        return transactions.map(transactionList -> transactionList.stream()
                .map(Transaction::getBundle)
                .mapToDouble(Bundle::getWeight)
                .sum());
    }

    @Override
    public Optional<Double> totalBoughtByClass(long classId) {
        Optional<List<Transaction>> transactions = transactionRepository.findByBundleClassFieldId(classId);
        return transactions.map(transactionList -> transactionList.stream()
                .map(Transaction::getBundle)
                .mapToDouble(Bundle::getWeight)
                .sum());
    }

    @Override
    public Optional<Double> totalBought() {
        List<Transaction> transactions = transactionRepository.findAll();
        return Optional.of(transactions.stream()
                .map(Transaction::getBundle)
                .mapToDouble(Bundle::getWeight)
                .sum());
    }

    @Override
    public Optional<Double> totalBoughtBetween(Instant from, Instant to) {
         Optional<List<Transaction>> transactions = transactionRepository.findByBundleBoughtAtBetween(from, to);
        return transactions.map(transactionList -> transactionList.stream()
                .map(Transaction::getBundle)
                .mapToDouble(Bundle::getWeight)
                .sum());
    }
}
