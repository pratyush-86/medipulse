package com.medipulse.model;

public class Patient extends User {
    public Patient(int id, String name, String email, String phone) {
        super(id, name, email, phone);
    }
    @Override public String getRole() { return "PATIENT"; }
    @Override public String getRoleLabel() { return "Patient"; }
}
