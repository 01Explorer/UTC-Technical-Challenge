package com.challenge.tobacco.application.services;

import com.challenge.tobacco.domain.entities.Address;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.exceptions.AddressException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;

import java.util.List;
import java.util.Optional;

public interface ProducerService {
    long createProducer(Producer producer) throws InvalidProducerException;
    Optional<Producer> findById(long id);
    Optional<List<Producer>> getAllProducers();
    long updateProducer(Producer newProducer, Producer oldProducer) throws InvalidProducerException, AddressException;
    long deleteProducer(long id) throws InvalidProducerException;
    Address getAddress(String cep) throws AddressException;
    long patchProducer(Producer newProducer, Producer oldProducer) throws InvalidProducerException, AddressException;
}
