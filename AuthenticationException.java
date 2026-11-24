package com.medipulse.exception;

/** Thrown when login fails. */
public class AuthenticationException extends MediPulseException {
    public AuthenticationException(String message) { super(message); }
}
