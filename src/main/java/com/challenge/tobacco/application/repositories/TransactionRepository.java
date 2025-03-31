package com.challenge.tobacco.application.repositories;

import com.challenge.tobacco.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByBundleProducerId(Long producerId);
    Optional<List<Transaction>> findByBundleClassFieldId(Long classId);
    Optional<List<Transaction>> findByBundleBoughtAtBetween(Instant from, Instant to);
    Optional<Transaction> findByBundleId(Long id);
}
