package com.medipulse.exception;

/** Thrown when user input is invalid. */
public class ValidationException extends MediPulseException {
    public ValidationException(String message) { super(message); }
}
