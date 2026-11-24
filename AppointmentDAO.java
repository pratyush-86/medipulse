package com.medipulse.dao;

import com.medipulse.exception.*;
import com.medipulse.model.Appointment;
import com.medipulse.model.AppointmentStatus;
import com.medipulse.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO implements GenericDAO<Appointment> {
    private static final String BASE =
        "SELECT a.id, p.name, d.name, a.appt_date, a.appt_time, a.status, a.notes " +
        "FROM appointments a JOIN users p ON a.patient_id=p.id JOIN users d ON a.doctor_id=d.id ";

    public void insert(int patientId, int doctorId, String date, String time, String notes)
            throws MediPulseException {
        String sql = "INSERT INTO appointments(patient_id,doctor_id,appt_date,appt_time,notes) VALUES(?,?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, notes);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not book appointment", e);
        }
    }

    /** True if the doctor already has an active appointment in this slot. */
    public boolean isSlotTaken(int doctorId, String date, String time) throws MediPulseException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appt_date=? AND appt_time=? AND status<>'CANCELLED'";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setString(2, date);
            ps.setString(3, time);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not check slot", e);
        }
    }

    @Override
    public List<Appointment> findAll() throws MediPulseException {
        return query(BASE + "ORDER BY a.appt_date DESC, a.appt_time DESC", 0);
    }

    public List<Appointment> findByPatient(int id) throws MediPulseException {
        return query(BASE + "WHERE a.patient_id=? ORDER BY a.appt_date DESC", id);
    }

    public List<Appointment> findByDoctor(int id) throws MediPulseException {
        return query(BASE + "WHERE a.doctor_id=? ORDER BY a.appt_date DESC", id);
    }

    public boolean updateStatus(int id, AppointmentStatus status) throws MediPulseException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("UPDATE appointments SET status=? WHERE id=?")) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not update appointment", e);
        }
    }

    @Override
    public boolean delete(int id) throws MediPulseException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM appointments WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete appointment", e);
        }
    }

    private List<Appointment> query(String sql, int param) throws MediPulseException {
        List<Appointment> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param > 0) {
                ps.setInt(1, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load appointments", e);
        }
        return list;
    }
}
