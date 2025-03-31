package com.challenge.tobacco.application.services.impl;

import com.challenge.tobacco.application.repositories.ProducerRepository;
import com.challenge.tobacco.application.services.ProducerService;
import com.challenge.tobacco.domain.entities.Address;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.exceptions.AddressException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class ProducerServiceImpl implements ProducerService {

    private static String ADDRESS_CLIENT_URL = "https://brasilapi.com.br/api/cep/v1/{cep}";

    private final ProducerRepository producerRepository;
    private final RestClient addressClient;

    public ProducerServiceImpl(ProducerRepository producerRepository, RestClient.Builder addressClient) {
        this.producerRepository = producerRepository;
        this.addressClient = addressClient
                .build();
    }

    @Override
    public long createProducer(Producer producer) throws InvalidProducerException {
        if (cpfAlreadyExists(producer.getCpf())) {
            throw new InvalidProducerException(HttpStatus.CONFLICT, String.format("Producer with CPF: %s already exists", producer.getCpf()));
        }
        return producerRepository.save(producer).getId();
    }

    @Override
    public Optional<Producer> findById(long id) {
        return producerRepository.findById(id);
    }

    @Override
    public Optional<List<Producer>> getAllProducers() {
        return Optional.of(producerRepository.findAll());
    }

    @Override
    public long updateProducer(Producer newProducer, Producer oldProducer) throws InvalidProducerException, AddressException {
        if (!oldProducer.getCpf().equals(newProducer.getCpf()) && cpfAlreadyExists(newProducer.getCpf())) {
            throw new InvalidProducerException(HttpStatus.CONFLICT, String.format("Producer with CPF: %s already exists", newProducer.getCpf()));
        }
        if(!oldProducer.getAddress().cep().equals(newProducer.getAddress().cep())) {
            oldProducer.setAddress(getAddress(newProducer.getAddress().cep()));
        }
        oldProducer.setName(newProducer.getName());
        oldProducer.setCpf(newProducer.getCpf());
        return producerRepository.save(oldProducer).getId();
    }

    @Override
    public long deleteProducer(long id) throws InvalidProducerException {
        Optional<Producer> producer = producerRepository.findById(id);
        if (producer.isPresent()) {
            producerRepository.delete(producer.get());
            return id;
        }
        throw new InvalidProducerException(HttpStatus.NOT_FOUND, String.format("Producer with ID: %s not found", id));
    }

    @Override
    public Address getAddress(String cep) throws AddressException {
        try{
            if (!StringUtils.hasText(cep)){
                throw new AddressException(HttpStatus.UNPROCESSABLE_ENTITY, "CEP can't be blank");
            }
            return addressClient.get()
                    .uri(ADDRESS_CLIENT_URL, cep)
                    .retrieve()
                    .body(Address.class);
        } catch (HttpClientErrorException e) {
            throw new AddressException(HttpStatus.valueOf(e.getStatusCode().value()),e.getResponseBodyAsString());
        }

    }

    @Override
    public long patchProducer(Producer newProducer, Producer oldProducer) throws InvalidProducerException, AddressException {
        if (!StringUtils.hasText(newProducer.getCpf())) {
            newProducer.setCpf(oldProducer.getCpf());
        }

        if(!StringUtils.hasText(newProducer.getAddress().cep())){
            newProducer.setAddress(oldProducer.getAddress());
        }

        if(!StringUtils.hasText(newProducer.getName())){
            newProducer.setName(oldProducer.getName());
        }

        return updateProducer(newProducer, oldProducer);
    }

    private Boolean cpfAlreadyExists(String cpf) {
        Optional<Producer> maybeProducer = producerRepository.findByCpf(cpf);
        return maybeProducer.isPresent();
    }
}
