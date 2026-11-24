package com.medipulse.model;

/** Factory: builds the correct User subclass from a database role string. */
public final class UserFactory {
    private UserFactory() { }

    public static User create(int id, String name, String email, String phone, String role, String specialization) {
        switch (role) {
            case "ADMIN":  return new Admin(id, name, email, phone);
            case "DOCTOR": return new Doctor(id, name, email, phone, specialization);
            default:       return new Patient(id, name, email, phone);
        }
    }
}
