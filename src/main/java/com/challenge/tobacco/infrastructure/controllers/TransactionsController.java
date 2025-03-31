package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.dtos.TransactionDTO;
import com.challenge.tobacco.application.enums.ResponseStatus;
import com.challenge.tobacco.application.services.BundleService;
import com.challenge.tobacco.application.services.TransactionService;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.entities.Transaction;
import com.challenge.tobacco.domain.exceptions.InvalidTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TransactionsController {
    private final TransactionService transactionService;
    private final BundleService bundleService;

    public TransactionsController(TransactionService transactionService, BundleService bundleService) {
        this.transactionService = transactionService;
        this.bundleService = bundleService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<Response> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            Transaction transaction = dtoToEntity(transactionDTO);
            long output = transactionService.create(transaction);
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("transactionId", output )), HttpStatus.CREATED);
        } catch (InvalidTransactionException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.error, null, Map.of("error", e.getMessage())), e.getStatus());
        }

    }

    @GetMapping("/transaction")
    public ResponseEntity<Response> getTotalBoughtByProducer(@RequestParam(required = false) Long producerId, @RequestParam(required = false) Long classId, @RequestParam(required = false) Instant from, @RequestParam(required = false) Instant to) {
        Double output = null;
        if (Objects.nonNull(producerId)){
            Optional<Double> totalBoughtByProducer = transactionService.totalBoughtByProducer(producerId);
            output = totalBoughtByProducer.orElse(0.);
        }
        if (Objects.nonNull(classId)){
            Optional<Double> totalBoughtByClass = transactionService.totalBoughtByClass(classId);
            output = totalBoughtByClass.orElse(0.);
        }
        if (Objects.nonNull(from) && Objects.nonNull(to)){
            Optional<Double> totalBoughtBetween = transactionService.totalBoughtBetween(from, to);
            output = totalBoughtBetween.orElse(0.);
        }
        if (Objects.isNull(output)){
            Optional<Double> totalBought = transactionService.totalBought();
            output = totalBought.orElse(0.);
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("totalBought", output )), HttpStatus.OK);
    }


    private Transaction dtoToEntity(TransactionDTO dto) throws InvalidTransactionException {
        Optional<Bundle> maybeBundle = bundleService.findById(dto.bundleId());
        if (maybeBundle.isEmpty()) {
            throw new InvalidTransactionException(HttpStatus.NOT_FOUND,"Bundle not found");
        }
        return new Transaction(maybeBundle.get());
    }


}
