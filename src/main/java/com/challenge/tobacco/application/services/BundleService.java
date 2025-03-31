package com.challenge.tobacco.application.services;

import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.exceptions.InvalidBundleException;

import java.util.List;
import java.util.Optional;

public interface BundleService {
    long createBundle(Bundle bundle);
    Optional<Bundle> findById(long id);
    Optional<List<Bundle>> findAll();
    long updateBundle(Bundle newBundle, Bundle oldBundle);
    long deleteBundle(long id) throws InvalidBundleException;
    long patchBundle(Bundle newBundle, Bundle oldBundle);
}
