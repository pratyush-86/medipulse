package com.medipulse.exception;

/** Base checked exception for the whole application. */
public class MediPulseException extends Exception {
    public MediPulseException(String message) { super(message); }
    public MediPulseException(String message, Throwable cause) { super(message, cause); }
}
