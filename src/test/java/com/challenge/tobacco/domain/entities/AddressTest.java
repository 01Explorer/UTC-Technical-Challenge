package com.challenge.tobacco.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressTest {

    @Test
    void constructor_ShouldCreateAddressWithAllFields() {
        // Arrange
        String cep = "12345-678";
        String state = "SP";
        String city = "São Paulo";
        String neighborhood = "Centro";
        String street = "Avenida Paulista";

        // Act
        Address address = new Address(cep, state, city, neighborhood, street);

        // Assert
        assertEquals(cep, address.cep());
        assertEquals(state, address.state());
        assertEquals(city, address.city());
        assertEquals(neighborhood, address.neighborhood());
        assertEquals(street, address.street());
    }

    @Test
    void fromCep_ShouldCreateAddressWithOnlyCepField() {
        // Arrange
        String cep = "12345-678";

        // Act
        Address address = Address.fromCep(cep);

        // Assert
        assertEquals(cep, address.cep());
        assertNull(address.state());
        assertNull(address.city());
        assertNull(address.neighborhood());
        assertNull(address.street());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345-678", "87654-321", "00000-000"})
    void fromCep_WithDifferentCeps_ShouldCreateAddressWithCorrectCep(String cep) {
        // Act
        Address address = Address.fromCep(cep);

        // Assert
        assertEquals(cep, address.cep());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void fromCep_WithInvalidInput_ShouldStillCreateAddress(String cep) {
        // Act
        Address address = Address.fromCep(cep);

        // Assert
        assertEquals(cep, address.cep());
    }

    @Test
    void equals_WithSameValues_ShouldBeEqual() {
        // Arrange
        Address address1 = new Address("12345-678", "SP", "São Paulo", "Centro", "Avenida Paulista");
        Address address2 = new Address("12345-678", "SP", "São Paulo", "Centro", "Avenida Paulista");

        // Assert
        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    void equals_WithDifferentValues_ShouldNotBeEqual() {
        // Arrange
        Address address1 = new Address("12345-678", "SP", "São Paulo", "Centro", "Avenida Paulista");
        Address address2 = new Address("87654-321", "RJ", "Rio de Janeiro", "Copacabana", "Avenida Atlântica");

        // Assert
        assertNotEquals(address1, address2);
    }

    @Test
    void toString_ShouldContainAllFieldValues() {
        // Arrange
        Address address = new Address("12345-678", "SP", "São Paulo", "Centro", "Avenida Paulista");

        // Act
        String toString = address.toString();

        // Assert
        assertTrue(toString.contains("12345-678"));
        assertTrue(toString.contains("SP"));
        assertTrue(toString.contains("São Paulo"));
        assertTrue(toString.contains("Centro"));
        assertTrue(toString.contains("Avenida Paulista"));
    }
}