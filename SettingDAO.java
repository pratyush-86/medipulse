package com.medipulse.dao;

import com.medipulse.exception.*;
import com.medipulse.util.DBConnection;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/** Key/value system settings (clinic name, slot length, working hours). */
public class SettingDAO {

    public Map<String, String> getAll() throws MediPulseException {
        Map<String, String> map = new LinkedHashMap<>();
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("SELECT setting_key, setting_value FROM settings");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load settings", e);
        }
        return map;
    }

    public void save(Map<String, String> settings) throws MediPulseException {
        String sql = "INSERT INTO settings(setting_key,setting_value) VALUES(?,?) " +
                     "ON DUPLICATE KEY UPDATE setting_value=VALUES(setting_value)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (Map.Entry<String, String> e : settings.entrySet()) {
                ps.setString(1, e.getKey());
                ps.setString(2, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save settings", e);
        }
    }
}
