package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.BundleDTO;
import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.enums.ResponseStatus;
import com.challenge.tobacco.application.services.BundleService;
import com.challenge.tobacco.application.services.ProducerService;
import com.challenge.tobacco.application.services.TobaccoClassService;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidBundleException;
import com.challenge.tobacco.domain.exceptions.InvalidProducerException;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class BundleController {
    private final BundleService bundleService;
    private final ProducerService producerService;
    private final TobaccoClassService tobaccoClassService;

    public BundleController(BundleService bundleService, ProducerService producerService, TobaccoClassService tobaccoClassService) {
        this.bundleService = bundleService;
        this.producerService = producerService;
        this.tobaccoClassService = tobaccoClassService;
    }

    @GetMapping("/bundle/{id}")
    public ResponseEntity<Response> getBundleById(@PathVariable Long id) {
        Optional<Bundle> bundle = bundleService.findById(id);
        return bundle.map(value -> new ResponseEntity<>( new Response(ResponseStatus.success, null, Map.of("bundle", bundle.get())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>( new Response(ResponseStatus.success, null, null), HttpStatus.NOT_FOUND));
    }

    @GetMapping("/bundle")
    public ResponseEntity<Response> getBundles() {
        Optional<List<Bundle>> bundle = bundleService.findAll();
        return bundle.map(value -> new ResponseEntity<>( new Response(ResponseStatus.success, null, Map.of("bundles", bundle.get())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>( new Response(ResponseStatus.success, null, null), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/bundle")
    public ResponseEntity<Response> createBundle(@RequestBody BundleDTO bundleDTO) {
        try {
            Bundle bundle = dtoToEntity(bundleDTO);
            long output = bundleService.createBundle(bundle);
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("bundleId", output )), HttpStatus.CREATED);
        } catch (IllegalArgumentException | InvalidTobaccoClassException | InvalidProducerException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.error, null, Map.of("error", e.getMessage())), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/bundle/{id}")
    public ResponseEntity<Response> updateBundle(@PathVariable Long id, @RequestBody BundleDTO bundleDTO) {
        Optional<Bundle> oldBundle = bundleService.findById(id);
        if (oldBundle.isEmpty()) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, null), HttpStatus.NOT_FOUND);
        }
        try {
            Bundle newBundle = dtoToEntity(bundleDTO);
            bundleService.updateBundle(newBundle, oldBundle.get());
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("bundleId", id)), HttpStatus.OK);
        } catch (IllegalArgumentException | InvalidTobaccoClassException | InvalidProducerException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.error, null, Map.of("error", e.getMessage())), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/bundle/{id}")
    public ResponseEntity<Response> deleteBundle(@PathVariable Long id) {
        try {
            bundleService.deleteBundle(id);
        } catch (InvalidBundleException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, Map.of("error", e.getMessage())), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("bundleId", id)), HttpStatus.OK);
    }

    @PatchMapping("/bundle/{id}")
    public ResponseEntity<Response> patchBundle(@PathVariable Long id, @RequestBody BundleDTO bundleDTO) {
        Optional<Bundle> oldBundle = bundleService.findById(id);
        if (oldBundle.isEmpty()) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, null), HttpStatus.NOT_FOUND);
        }
        try {
            Bundle newBundle = patchDtoToEntity(bundleDTO, oldBundle.get());
            bundleService.patchBundle(newBundle, oldBundle.get());
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("bundleId", id)), HttpStatus.OK);
        } catch (IllegalArgumentException | InvalidTobaccoClassException | InvalidProducerException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.error, null, Map.of("error", e.getMessage())), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private Bundle dtoToEntity(BundleDTO bundleDTO) throws InvalidProducerException, InvalidTobaccoClassException {
        Optional<Producer> producer = producerService.findById(bundleDTO.producerID());
        if (producer.isEmpty()) {
            throw new InvalidProducerException(HttpStatus.NOT_FOUND, "Producer not found");
        }
        Optional<TobaccoClass> tobaccoClass = tobaccoClassService.findById(bundleDTO.classID());
        if (tobaccoClass.isEmpty()) {
            throw new InvalidTobaccoClassException(HttpStatus.NOT_FOUND, "Tobacco class not found");
        }
        return new Bundle(bundleDTO.label(), bundleDTO.boughtAt(), producer.get(), tobaccoClass.get(), bundleDTO.weight());
    }

    private Bundle patchDtoToEntity(BundleDTO bundleDTO, Bundle oldBundle) throws InvalidProducerException, InvalidTobaccoClassException {
        Optional<Producer> producer = producerService.findById(bundleDTO.producerID());
        if (producer.isEmpty()) {
            producer = Optional.of(oldBundle.getProducer());
        }
        Optional<TobaccoClass> tobaccoClass = tobaccoClassService.findById(bundleDTO.classID());
        if (tobaccoClass.isEmpty()) {
            tobaccoClass = Optional.of(oldBundle.getClassField());
        }
        String label = StringUtils.hasText(bundleDTO.label()) ? bundleDTO.label() : oldBundle.getLabel();
        Instant boughtAt = Objects.isNull(bundleDTO.boughtAt()) ? oldBundle.getBoughtAt() : bundleDTO.boughtAt();
        Double weight = Objects.isNull(bundleDTO.weight()) ? oldBundle.getWeight() : bundleDTO.weight();
        return new Bundle(label, boughtAt, producer.get(), tobaccoClass.get(), weight);
    }






}
