package com.challenge.tobacco.application.services.impl;

import com.challenge.tobacco.application.repositories.TobaccoClassRepository;
import com.challenge.tobacco.application.services.TobaccoClassService;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TobaccoClassServiceImpl implements TobaccoClassService {

    private final TobaccoClassRepository tobaccoClassRepository;

    public TobaccoClassServiceImpl(TobaccoClassRepository tobaccoClassRepository) {
        this.tobaccoClassRepository = tobaccoClassRepository;
    }

    @Override
    public long createClass(TobaccoClass tobaccoClass) {
        return tobaccoClassRepository.save(tobaccoClass).getId();
    }

    @Override
    public Optional<TobaccoClass> findById(Long id) {
        return tobaccoClassRepository.findById(id);
    }

    @Override
    public Optional<List<TobaccoClass>> findAll() {
        return Optional.of(tobaccoClassRepository.findAll());
    }

    @Override
    public long updateClass(TobaccoClass newClass, TobaccoClass oldClass) {
        oldClass.setDescription(newClass.getDescription());
        return tobaccoClassRepository.save(oldClass).getId();
    }

    @Override
    public long deleteClass(Long id) throws InvalidTobaccoClassException {
        Optional<TobaccoClass> tobaccoClass = tobaccoClassRepository.findById(id);
        if (tobaccoClass.isPresent()) {
            tobaccoClassRepository.deleteById(id);
            return id;
        }
        throw new InvalidTobaccoClassException(HttpStatus.NOT_FOUND, String.format("Tobacco Class with ID: %s, not found", id));
    }
}
