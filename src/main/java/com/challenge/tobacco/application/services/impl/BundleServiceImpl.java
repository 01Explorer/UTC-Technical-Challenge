package com.challenge.tobacco.application.services.impl;

import com.challenge.tobacco.application.repositories.BundleRepository;
import com.challenge.tobacco.application.services.BundleService;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.exceptions.InvalidBundleException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BundleServiceImpl implements BundleService {

    private final BundleRepository bundleRepository;

    public BundleServiceImpl(BundleRepository bundleRepository) {
        this.bundleRepository = bundleRepository;
    }

    @Override
    public long createBundle(Bundle bundle) {
        return bundleRepository.save(bundle).getId();
    }

    @Override
    public Optional<Bundle> findById(long id) {
        return bundleRepository.findById(id);
    }

    @Override
    public Optional<List<Bundle>> findAll() {
        return Optional.of(bundleRepository.findAll());
    }

    @Override
    public long updateBundle(Bundle newBundle, Bundle oldBundle) {
        oldBundle.setBoughtAt(newBundle.getBoughtAt());
        oldBundle.setLabel(newBundle.getLabel());
        oldBundle.setProducer(newBundle.getProducer());
        oldBundle.setClassField(newBundle.getClassField());
        oldBundle.setWeight(newBundle.getWeight());
        return bundleRepository.save(oldBundle).getId();
    }

    @Override
    public long deleteBundle(long id) throws InvalidBundleException {
        Optional<Bundle> optionalBundle = bundleRepository.findById(id);
        if (optionalBundle.isPresent()) {
            bundleRepository.delete(optionalBundle.get());
            return id;
        }
        throw new InvalidBundleException(HttpStatus.NOT_FOUND, String.format("Bundle with ID: %s, not found", id));
    }

    @Override
    public long patchBundle(Bundle newBundle, Bundle oldBundle) {
        if (StringUtils.hasText(newBundle.getLabel())) {
            oldBundle.setLabel(newBundle.getLabel());
        }
        if (!oldBundle.getBoughtAt().equals(newBundle.getBoughtAt())) {
            oldBundle.setBoughtAt(newBundle.getBoughtAt());
        }
        if(!oldBundle.getWeight().equals(newBundle.getWeight())) {
            oldBundle.setWeight(newBundle.getWeight());
        }
        if (!oldBundle.getProducer().equals(newBundle.getProducer())) {
            oldBundle.setProducer(newBundle.getProducer());
        }
        if (!oldBundle.getClassField().equals(newBundle.getClassField())) {
            oldBundle.setClassField(newBundle.getClassField());
        }
        return bundleRepository.save(oldBundle).getId();
    }
}
