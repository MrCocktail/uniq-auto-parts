package com.dave.dev.jakarta.app.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ValidationUtilTest {

    @Test
    void requireEmail_acceptsValidEmail() {
        assertDoesNotThrow(() -> ValidationUtil.requireEmail("test@example.com", "email"));
    }

    @Test
    void requireEmail_rejectsInvalidEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireEmail("not-an-email", "email"));
    }

    @Test
    void requireNonNegative_rejectsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonNegative(new BigDecimal("-1"), "price"));
    }

    @Test
    void requireNonNegative_acceptsZero() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonNegative(BigDecimal.ZERO, "price"));
    }

    @Test
    void requirePositiveInt_rejectsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requirePositiveInt(0, "qty"));
    }
}
