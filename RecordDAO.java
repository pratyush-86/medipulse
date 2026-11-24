package com.medipulse.dao;

import com.medipulse.exception.*;
import com.medipulse.model.MedicalRecord;
import com.medipulse.util.DBConnection;
import com.medipulse.util.Validator;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO implements GenericDAO<MedicalRecord> {
    private static final String BASE =
        "SELECT r.id, p.name, d.name, r.diagnosis, r.treatment, DATE(r.created_at) " +
        "FROM medical_records r JOIN users p ON r.patient_id=p.id JOIN users d ON r.doctor_id=d.id ";

    public void add(int patientId, int doctorId, String diagnosis, String treatment) throws MediPulseException {
        Validator.required(diagnosis, "Diagnosis");
        String sql = "INSERT INTO medical_records(patient_id,doctor_id,diagnosis,treatment) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, diagnosis.trim());
            ps.setString(4, treatment);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save record", e);
        }
    }

    @Override
    public List<MedicalRecord> findAll() throws MediPulseException {
        return query(BASE + "ORDER BY r.created_at DESC", 0);
    }

    public List<MedicalRecord> findByPatient(int id) throws MediPulseException {
        return query(BASE + "WHERE r.patient_id=? ORDER BY r.created_at DESC", id);
    }

    public List<MedicalRecord> findByDoctor(int id) throws MediPulseException {
        return query(BASE + "WHERE r.doctor_id=? ORDER BY r.created_at DESC", id);
    }

    @Override
    public boolean delete(int id) throws MediPulseException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM medical_records WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete record", e);
        }
    }

    private List<MedicalRecord> query(String sql, int id) throws MediPulseException {
        List<MedicalRecord> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (id > 0) {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MedicalRecord(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6)));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load records", e);
        }
        return list;
    }
}
