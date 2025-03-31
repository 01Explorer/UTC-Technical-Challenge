package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.ProducerDTO;
import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.enums.ResponseStatus;
import com.challenge.tobacco.application.services.ProducerService;
import com.challenge.tobacco.domain.entities.Address;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.exceptions.AddressException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class ProducerController {
    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/producer/{id}")
    public ResponseEntity<Response> getProducer(@PathVariable Long id) {
        Optional<Producer> producer = producerService.findById(id);
        return producer.map(value -> new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("producer", value)), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(ResponseStatus.success, null, null), HttpStatus.NOT_FOUND));
    }

    @GetMapping("/producer")
    public ResponseEntity<Response> getProducers() {
        Optional<List<Producer>> producers = producerService.getAllProducers();
        return producers.map(producerList -> new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("producers", producerList)), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(ResponseStatus.success, null, null), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/producer")
    public ResponseEntity<Response> createProducer(@RequestBody ProducerDTO producerDTO) {
        long output;
        try {
            Address address = producerService.getAddress(producerDTO.cep());
            Producer producer = dtoToEntity(producerDTO, address);
            output = producerService.createProducer(producer);
        } catch (InvalidProducerException | AddressException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, Map.of("error", e.getMessage())), e.getStatus());
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("producerId", output)), HttpStatus.CREATED);
    }

    @PutMapping("/producer/{id}")
    public ResponseEntity<Response> updateProducer(@PathVariable Long id, @RequestBody ProducerDTO producerDTO) {
        Optional<Producer> producer = producerService.findById(id);
        if (producer.isEmpty()) {
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, null), HttpStatus.NOT_FOUND);
        }
        Producer newProducer = dtoToEntity(producerDTO, Address.fromCep(producerDTO.cep()));
        try {
            producerService.updateProducer(newProducer, producer.get());
        } catch (InvalidProducerException | AddressException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, Map.of("error", e.getMessage())), e.getStatus());
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("producerId", id)), HttpStatus.OK);
    }

    @PatchMapping("/producer/{id}")
    public ResponseEntity<Response> patchProducer(@PathVariable Long id, @RequestBody ProducerDTO producerDTO) {
        Optional<Producer> producer = producerService.findById(id);
        if (producer.isEmpty()) {
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, null), HttpStatus.NOT_FOUND);
        }
        Producer newProducer = dtoToEntity(producerDTO, Address.fromCep(producerDTO.cep()));
        try {
            producerService.patchProducer(newProducer, producer.get());
        } catch (InvalidProducerException | AddressException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, Map.of("error", e.getMessage())), e.getStatus());
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("producerId", id)), HttpStatus.OK);
    }

    @DeleteMapping("/producer/{id}")
    public ResponseEntity<Response> deleteProducer(@PathVariable Long id) {
        long output;
        try {
            output = producerService.deleteProducer(id);
        } catch (InvalidProducerException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, Map.of("error", e.getMessage())), e.getStatus());
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("producerId", output)), HttpStatus.OK);
    }

    private Producer dtoToEntity(ProducerDTO producerDTO, Address address) {
        Producer producer = new Producer();
        producer.setCpf(producerDTO.cpf());
        producer.setName(producerDTO.name());
        producer.setAddress(address);
        return producer;
    }
}
