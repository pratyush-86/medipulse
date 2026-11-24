package com.medipulse.exception;

/** Thrown when a doctor slot is already booked. */
public class BookingConflictException extends MediPulseException {
    public BookingConflictException(String message) { super(message); }
}
