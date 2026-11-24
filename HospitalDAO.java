package com.medipulse.dao;

import com.medipulse.exception.*;
import com.medipulse.model.Hospital;
import com.medipulse.util.DBConnection;
import com.medipulse.util.Validator;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Powers the "nearby hospitals" search. */
public class HospitalDAO implements GenericDAO<Hospital> {

    public void add(String name, String city, String address, String phone, String type) throws MediPulseException {
        Validator.required(name, "Hospital name");
        Validator.required(city, "City");
        String sql = "INSERT INTO hospitals(name,city,address,phone,type) VALUES(?,?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setString(2, city.trim());
            ps.setString(3, address);
            ps.setString(4, phone);
            ps.setString(5, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not add hospital", e);
        }
    }

    @Override
    public List<Hospital> findAll() throws MediPulseException {
        return search(null);
    }

    /** Case-insensitive search by city, name or type. Null/blank returns all. */
    public List<Hospital> search(String keyword) throws MediPulseException {
        boolean all = keyword == null || keyword.trim().isEmpty();
        String sql = "SELECT id,name,city,address,phone,type FROM hospitals " +
                     (all ? "" : "WHERE city LIKE ? OR name LIKE ? OR type LIKE ? ") + "ORDER BY city, name";
        List<Hospital> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (!all) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Hospital(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6)));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not search hospitals", e);
        }
        return list;
    }

    @Override
    public boolean delete(int id) throws MediPulseException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM hospitals WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete hospital", e);
        }
    }
}
