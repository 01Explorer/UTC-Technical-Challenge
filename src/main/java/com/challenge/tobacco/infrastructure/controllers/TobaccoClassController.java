package com.challenge.tobacco.infrastructure.controllers;

import com.challenge.tobacco.application.dtos.Response;
import com.challenge.tobacco.application.dtos.TobaccoClassDTO;
import com.challenge.tobacco.application.enums.ResponseStatus;
import com.challenge.tobacco.application.services.TobaccoClassService;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController()
@RequestMapping("api/v1")
public class TobaccoClassController {
    private final TobaccoClassService tobaccoClassService;

    public TobaccoClassController(TobaccoClassService tobaccoClassService) {
        this.tobaccoClassService = tobaccoClassService;
    }

    @GetMapping("/class/{id}")
    public ResponseEntity<Response> getClassById(@PathVariable Long id) {
            Optional<TobaccoClass> tobaccoClass = tobaccoClassService.findById(id);
            return tobaccoClass.map(value -> new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("class", tobaccoClass.get())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(ResponseStatus.success, null, null),HttpStatus.NOT_FOUND));
    }

    @GetMapping("/class")
    public ResponseEntity<Response> getClasses() {
        Optional<List<TobaccoClass>> classes = tobaccoClassService.findAll();
        return classes.map(value -> new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("classes", classes.get())), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(ResponseStatus.success, null, null),HttpStatus.NOT_FOUND));
    }

    @PostMapping("/class")
    public ResponseEntity<Response> createClass(@RequestBody TobaccoClassDTO tobaccoClassDTO) {
        try{
            TobaccoClass tobaccoClass = dtoToEntity(tobaccoClassDTO);
            long output = tobaccoClassService.createClass(tobaccoClass);
            return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("classId", output)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(ResponseStatus.error, null, Map.of("error", e.getMessage())), HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @PutMapping("/class/{id}")
    public ResponseEntity<Response> updateClass(@PathVariable Long id, @RequestBody TobaccoClassDTO tobaccoClassDTO) {
        Optional<TobaccoClass> oldTobaccoClass = tobaccoClassService.findById(id);
        if(oldTobaccoClass.isEmpty()) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, null), HttpStatus.NOT_FOUND);
        }
        TobaccoClass newTobaccoClass = dtoToEntity(tobaccoClassDTO);
        tobaccoClassService.updateClass(newTobaccoClass, oldTobaccoClass.get());
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("classId", id)), HttpStatus.OK);
    }

    @DeleteMapping("/class/{id}")
    public ResponseEntity<Response> deleteClass(@PathVariable Long id) {
        try {
            tobaccoClassService.deleteClass(id);
        } catch (InvalidTobaccoClassException e) {
            return new ResponseEntity<>(new Response(ResponseStatus.failure, null, Map.of("error", e.getMessage())), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new Response(ResponseStatus.success, null, Map.of("classId", id)), HttpStatus.OK);
    }

    private TobaccoClass dtoToEntity(TobaccoClassDTO tobaccoClassDTO){
        return new TobaccoClass(tobaccoClassDTO.description());
    }
}
