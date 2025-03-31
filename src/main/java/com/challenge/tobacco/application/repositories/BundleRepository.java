package com.challenge.tobacco.application.repositories;

import com.challenge.tobacco.domain.entities.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BundleRepository extends JpaRepository<Bundle, Long> {
    Optional<Bundle> findById(long id);
}
