package com.medipulse.model;

/** Abstract parent of Admin, Doctor and Patient (inheritance + polymorphism). */
public abstract class User implements Dashboardable {
    protected final int id;
    protected String name;
    protected final String email;
    protected String phone;

    protected User(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /** Role name as stored in the database. */
    public abstract String getRole();

    @Override
    public String getDashboardPath() {
        return "/" + getRole().toLowerCase();
    }

    public String getInitials() {
        String[] parts = name.trim().split("\\s+");
        String s = parts[0].substring(0, 1);
        if (parts.length > 1) {
            s += parts[parts.length - 1].substring(0, 1);
        }
        return s.toUpperCase();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone == null ? "" : phone; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return getRole() + "[" + id + ", " + name + "]";
    }
}
