package com.medipulse.model;

public class Appointment {
    private final int id;
    private final String patientName, doctorName, date, time, status, notes;

    public Appointment(int id, String patientName, String doctorName,
                       String date, String time, String status, String notes) {
        this.id = id; this.patientName = patientName; this.doctorName = doctorName;
        this.date = date; this.time = time; this.status = status; this.notes = notes;
    }
    public int getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public String getNotes() { return notes == null ? "" : notes; }
}
