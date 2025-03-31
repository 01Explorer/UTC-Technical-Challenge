package com.challenge.tobacco.application.services.impl;

import com.challenge.tobacco.application.repositories.BundleRepository;
import com.challenge.tobacco.domain.entities.Bundle;
import com.challenge.tobacco.domain.entities.Producer;
import com.challenge.tobacco.domain.entities.TobaccoClass;
import com.challenge.tobacco.domain.exceptions.InvalidBundleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BundleServiceImplTest {

    @Mock
    private BundleRepository bundleRepository;

    @Mock
    private Producer producer;

    @Mock
    private TobaccoClass tobaccoClass;

    @InjectMocks
    private BundleServiceImpl bundleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBundle() {
        // Arrange
        Bundle bundle = mock(Bundle.class);
        when(bundle.getId()).thenReturn(1L);
        when(bundleRepository.save(bundle)).thenReturn(bundle);

        // Act
        long result = bundleService.createBundle(bundle);

        // Assert
        assertEquals(1L, result);
        verify(bundleRepository, times(1)).save(bundle);
    }

    @Test
    void testFindById() {
        // Arrange
        Bundle bundle = mock(Bundle.class);
        when(bundle.getId()).thenReturn(1L);
        when(bundleRepository.findById(1L)).thenReturn(Optional.of(bundle));

        // Act
        Optional<Bundle> result = bundleService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bundle, result.get());
        verify(bundleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        // Arrange
        Bundle bundle = mock(Bundle.class);
        when(bundleRepository.findAll()).thenReturn(List.of(bundle));

        // Act
        Optional<List<Bundle>> result = bundleService.findAll();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(bundleRepository, times(1)).findAll();
    }

    @Test
    void testUpdateBundle() {
        // Arrange
        Bundle oldBundle = mock(Bundle.class);
        oldBundle.setId(1L);
        when(oldBundle.getId()).thenReturn(1L);
        Bundle newBundle = mock(Bundle.class);
        when(newBundle.getBoughtAt()).thenReturn(Instant.now());
        when(newBundle.getLabel()).thenReturn("New Label");
        when(bundleRepository.save(oldBundle)).thenReturn(oldBundle);

        // Act
        long result = bundleService.updateBundle(newBundle, oldBundle);

        // Assert
        assertEquals(1L, result);
        verify(bundleRepository, times(1)).save(oldBundle);
    }

    @Test
    void testDeleteBundle() throws InvalidBundleException {
        // Arrange
        Bundle bundle = mock(Bundle.class);
        when(bundle.getId()).thenReturn(1L);
        when(bundleRepository.findById(1L)).thenReturn(Optional.of(bundle));

        // Act
        long result = bundleService.deleteBundle(1L);

        // Assert
        assertEquals(1L, result);
        verify(bundleRepository, times(1)).findById(1L);
        verify(bundleRepository, times(1)).delete(bundle);
    }

    @Test
    void testDeleteBundleThrowsException() {
        // Arrange
        when(bundleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidBundleException exception = assertThrows(InvalidBundleException.class, () -> {
            bundleService.deleteBundle(1L);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Bundle with ID: 1, not found", exception.getMessage());
        verify(bundleRepository, times(1)).findById(1L);
    }

    @Test
    void testPatchBundle() {
        // Arrange
        Bundle oldBundle = mock(Bundle.class);
        oldBundle.setId(1L);
        when(oldBundle.getId()).thenReturn(1L);
        when(oldBundle.getLabel()).thenReturn("Old Label");
        when(oldBundle.getBoughtAt()).thenReturn(Instant.now());
        when(oldBundle.getWeight()).thenReturn(1.0);
        when(oldBundle.getProducer()).thenReturn(producer);
        when(oldBundle.getClassField()).thenReturn(tobaccoClass);

        Bundle newBundle = mock(Bundle.class);
        when(newBundle.getLabel()).thenReturn("New Label");
        when(newBundle.getBoughtAt()).thenReturn(Instant.now().plusSeconds(1));
        when(newBundle.getWeight()).thenReturn(2.0);
        when(newBundle.getProducer()).thenReturn(producer);
        when(newBundle.getClassField()).thenReturn(tobaccoClass);

        when(bundleRepository.save(oldBundle)).thenReturn(oldBundle);

        // Act
        long result = bundleService.patchBundle(newBundle, oldBundle);

        // Assert
        assertEquals(1L, result);
        verify(bundleRepository, times(1)).save(oldBundle);
    }
}
