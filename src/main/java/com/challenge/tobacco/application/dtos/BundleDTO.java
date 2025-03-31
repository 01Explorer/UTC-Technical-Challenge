package com.challenge.tobacco.application.dtos;

import java.time.Instant;

public record BundleDTO(String label, Instant boughtAt, long producerID, long classID, Double weight) {
}
