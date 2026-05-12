package com.dave.dev.jakarta.app.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private ValidationUtil() {
    }

    public static void requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireEmail(String value, String message) {
        if (value == null || value.isBlank() || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonNegative(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requirePositiveInt(Integer value, String message) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonNegativeInt(Integer value, String message) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
