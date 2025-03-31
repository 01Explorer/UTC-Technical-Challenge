package com.challenge.tobacco.application.services;

import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidTobaccoClassException;

import java.util.List;
import java.util.Optional;

public interface TobaccoClassService {
    long createClass(TobaccoClass classEntity);
    Optional<TobaccoClass> findById(Long id);
    Optional<List<TobaccoClass>> findAll();
    long updateClass(TobaccoClass newClass, TobaccoClass oldClass);
    long deleteClass(Long id) throws InvalidTobaccoClassException;
}
