package com.medipulse.model;

public class MedicalRecord {
    private final int id;
    private final String patientName, doctorName, diagnosis, treatment, date;

    public MedicalRecord(int id, String patientName, String doctorName,
                         String diagnosis, String treatment, String date) {
        this.id = id; this.patientName = patientName; this.doctorName = doctorName;
        this.diagnosis = diagnosis; this.treatment = treatment; this.date = date;
    }
    public int getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment == null ? "" : treatment; }
    public String getDate() { return date; }
}
