package com.challenge.tobacco.application.repositories;

import com.challenge.tobacco.domain.entities.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Optional<Producer> findById(long id);

    Optional<Producer> findByCpf(String cpf);
}