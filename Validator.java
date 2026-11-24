package com.medipulse.util;

import com.medipulse.exception.ValidationException;
import java.util.regex.Pattern;

/** Input validation helpers. Throws ValidationException on bad input. */
public final class Validator {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private Validator() { }

    public static String required(String value, String field) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " is required");
        }
        return value.trim();
    }

    public static String email(String value) throws ValidationException {
        String v = required(value, "Email");
        if (!EMAIL.matcher(v).matches()) {
            throw new ValidationException("Enter a valid email address");
        }
        return v.toLowerCase();
    }

    public static String password(String value) throws ValidationException {
        if (value == null || value.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
        return value;
    }
}
