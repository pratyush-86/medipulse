package com.medipulse.model;

public class Hospital {
    private final int id;
    private final String name, city, address, phone, type;

    public Hospital(int id, String name, String city, String address, String phone, String type) {
        this.id = id; this.name = name; this.city = city;
        this.address = address; this.phone = phone; this.type = type;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getAddress() { return address == null ? "" : address; }
    public String getPhone() { return phone == null ? "" : phone; }
    public String getType() { return type == null ? "" : type; }
}
