package com.challenge.tobacco.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @Mock
    private Bundle mockBundle;

    @Test
    void testParameterizedConstructor() {
        Transaction transaction = new Transaction(mockBundle);

        assertSame(mockBundle, transaction.getBundle());
        assertNull(transaction.getId());
        assertNull(transaction.getCreatedAt());
    }

    @Test
    void testProtectedConstructor() throws Exception {
        Transaction.class.getDeclaredConstructor().setAccessible(true);
        Transaction transaction = Transaction.class.getDeclaredConstructor().newInstance();

        assertNotNull(transaction);
        assertNull(transaction.getBundle());
        assertNull(transaction.getId());
        assertNull(transaction.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        Transaction transaction = new Transaction(null);
        Integer id = 1;
        Instant now = Instant.now();

        transaction.setId(id);
        transaction.setCreatedAt(now);
        transaction.setBundle(mockBundle);

        assertEquals(id, transaction.getId());
        assertEquals(now, transaction.getCreatedAt());
        assertSame(mockBundle, transaction.getBundle());
    }

    @Test
    void testCreatedAtAnnotation() {
        Transaction transaction = new Transaction(mockBundle);
        Instant testTime = Instant.now();

        transaction.setCreatedAt(testTime);

        assertEquals(testTime, transaction.getCreatedAt());
    }
}