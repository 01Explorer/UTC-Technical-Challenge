package com.challenge.tobacco.application.services;

import com.challenge.tobacco.domain.entities.Transaction;
import com.challenge.tobacco.domain.exceptions.InvalidTransactionException;

import java.time.Instant;
import java.util.Optional;

public interface TransactionService {
    long create(Transaction transaction) throws InvalidTransactionException;
    Optional<Double> totalBoughtByProducer(long producerId);
    Optional<Double> totalBoughtByClass(long classId);
    Optional<Double> totalBought();
    Optional<Double> totalBoughtBetween(Instant from, Instant to);
}
