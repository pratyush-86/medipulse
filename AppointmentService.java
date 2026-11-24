package com.medipulse.service;

import com.medipulse.dao.AppointmentDAO;
import com.medipulse.exception.*;
import com.medipulse.model.AppointmentStatus;
import com.medipulse.util.Validator;

/**
 * SYNCHRONIZATION: book() is synchronized on the shared instance so two
 * patients cannot grab the same doctor slot at the same moment.
 */
public class AppointmentService {
    private static final AppointmentService INSTANCE = new AppointmentService();
    private final AppointmentDAO dao = new AppointmentDAO();

    private AppointmentService() { }

    public static AppointmentService getInstance() {
        return INSTANCE;
    }

    public synchronized void book(int patientId, int doctorId, String date, String time, String notes)
            throws MediPulseException {
        Validator.required(date, "Date");
        Validator.required(time, "Time");
        if (dao.isSlotTaken(doctorId, date, time)) {
            throw new BookingConflictException("That slot is already booked. Please choose another time.");
        }
        dao.insert(patientId, doctorId, date, time, notes);
        NotificationService.getInstance().notifyAsync("New appointment booked for " + date + " at " + time);
    }

    public void changeStatus(int appointmentId, String status) throws MediPulseException {
        AppointmentStatus s;
        try {
            s = AppointmentStatus.valueOf(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValidationException("Invalid appointment status");
        }
        dao.updateStatus(appointmentId, s);
        NotificationService.getInstance().notifyAsync("Appointment #" + appointmentId + " marked " + s);
    }
}
