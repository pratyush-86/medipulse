package com.medipulse.dao;

import com.medipulse.exception.*;
import com.medipulse.model.Feedback;
import com.medipulse.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO implements GenericDAO<Feedback> {
    private static final String BASE =
        "SELECT f.id, p.name, d.name, f.rating, f.comments " +
        "FROM feedback f JOIN users p ON f.patient_id=p.id JOIN users d ON f.doctor_id=d.id ";

    public void add(int patientId, int doctorId, int rating, String comments) throws MediPulseException {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
        String sql = "INSERT INTO feedback(patient_id,doctor_id,rating,comments) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setInt(3, rating);
            ps.setString(4, comments);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save feedback", e);
        }
    }

    @Override
    public List<Feedback> findAll() throws MediPulseException {
        return query(BASE + "ORDER BY f.id DESC", 0);
    }

    public List<Feedback> findByDoctor(int doctorId) throws MediPulseException {
        return query(BASE + "WHERE f.doctor_id=? ORDER BY f.id DESC", doctorId);
    }

    @Override
    public boolean delete(int id) throws MediPulseException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM feedback WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete feedback", e);
        }
    }

    private List<Feedback> query(String sql, int id) throws MediPulseException {
        List<Feedback> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (id > 0) {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Feedback(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getInt(4), rs.getString(5)));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load feedback", e);
        }
        return list;
    }
}
