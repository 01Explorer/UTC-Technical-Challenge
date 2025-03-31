package com.challenge.tobacco.domain.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TobaccoClassTest {

    @Test
    void testParameterizedConstructor() {
        String description = "Sample description";

        TobaccoClass tobaccoClass = new TobaccoClass(description);

        assertEquals(description, tobaccoClass.getDescription());
        assertNull(tobaccoClass.getId());
    }

    @Test
    void testParameterizedConstructorWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TobaccoClass(null);
        });
    }

    @Test
    void testSetterAndGetters() {
        TobaccoClass tobaccoClass = new TobaccoClass("Initial description");
        String newDescription = "Updated description";
        Long id = 1L;

        tobaccoClass.setId(id);
        tobaccoClass.setDescription(newDescription);

        assertEquals(id, tobaccoClass.getId());
        assertEquals(newDescription, tobaccoClass.getDescription());
    }

    @Test
    void testProtectedConstructor() throws Exception {
        TobaccoClass.class.getDeclaredConstructor().setAccessible(true);
        TobaccoClass tobaccoClass = TobaccoClass.class.getDeclaredConstructor().newInstance();

        assertNotNull(tobaccoClass);
        assertNull(tobaccoClass.getDescription());
        assertNull(tobaccoClass.getId());
    }
}