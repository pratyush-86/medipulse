package com.medipulse.model;

public class Admin extends User {
    public Admin(int id, String name, String email, String phone) {
        super(id, name, email, phone);
    }
    @Override public String getRole() { return "ADMIN"; }
    @Override public String getRoleLabel() { return "Administrator"; }
}
