package com.medipulse.model;

public class Doctor extends User {
    private final String specialization;

    public Doctor(int id, String name, String email, String phone, String specialization) {
        super(id, name, email, phone);
        this.specialization = specialization == null ? "General" : specialization;
    }
    public String getSpecialization() { return specialization; }
    @Override public String getRole() { return "DOCTOR"; }
    @Override public String getRoleLabel() { return "Doctor (" + specialization + ")"; }
}
