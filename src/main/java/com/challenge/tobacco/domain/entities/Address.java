package com.challenge.tobacco.domain.entities;

public record Address(String cep, String state, String city, String neighborhood, String street) {
    public static Address fromCep(String cep) {
        return new Address(cep, null, null, null, null);
    }
}
