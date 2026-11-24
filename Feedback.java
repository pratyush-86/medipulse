package com.medipulse.model;

public class Feedback {
    private final int id, rating;
    private final String patientName, doctorName, comments;

    public Feedback(int id, String patientName, String doctorName, int rating, String comments) {
        this.id = id; this.patientName = patientName; this.doctorName = doctorName;
        this.rating = rating; this.comments = comments;
    }
    public int getId() { return id; }
    public int getRating() { return rating; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getComments() { return comments == null ? "" : comments; }
}
