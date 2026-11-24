package com.medipulse.exception;

/** Wraps SQLException so the web layer does not depend on JDBC types. */
public class DatabaseException extends MediPulseException {
    public DatabaseException(String message, Throwable cause) { super(message, cause); }
}
