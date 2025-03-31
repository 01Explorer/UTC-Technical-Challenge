package com.challenge.tobacco.application.repositories;

import com.challenge.tobacco.domain.entities.TobaccoClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TobaccoClassRepository extends JpaRepository<TobaccoClass, Long> {
    Optional<TobaccoClass> findById(long id);
}
