package com.medipulse.dao;

import com.medipulse.exception.*;
import com.medipulse.model.User;
import com.medipulse.model.UserFactory;
import com.medipulse.util.DBConnection;
import com.medipulse.util.PasswordUtil;
import com.medipulse.util.Validator;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserDAO implements GenericDAO<User> {
    private static final String COLS = "id,name,email,phone,specialization,role";

    private User map(ResultSet rs) throws SQLException {
        return UserFactory.create(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                rs.getString("phone"), rs.getString("role"), rs.getString("specialization"));
    }

    /** Verifies credentials and returns the matching User subclass. */
    public User login(String email, String password) throws MediPulseException {
        String sql = "SELECT " + COLS + " FROM users WHERE email=? AND password=?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, Validator.email(email));
            ps.setString(2, PasswordUtil.hash(password == null ? "" : password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error during login", e);
        }
        throw new AuthenticationException("Invalid email or password");
    }

    public void register(String name, String email, String password, String role,
                         String phone, String specialization) throws MediPulseException {
        Validator.required(name, "Name");
        String mail = Validator.email(email);
        Validator.password(password);
        String sql = "INSERT INTO users(name,email,password,phone,specialization,role) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setString(2, mail);
            ps.setString(3, PasswordUtil.hash(password));
            ps.setString(4, phone);
            ps.setString(5, "DOCTOR".equals(role) ? specialization : null);
            ps.setString(6, role);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ValidationException("This email is already registered");
        } catch (SQLException e) {
            throw new DatabaseException("Could not create user", e);
        }
    }

    public User findById(int id) throws MediPulseException {
        String sql = "SELECT " + COLS + " FROM users WHERE id=?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load user", e);
        }
        throw new ValidationException("User not found");
    }

    @Override
    public List<User> findAll() throws MediPulseException {
        return query("SELECT " + COLS + " FROM users ORDER BY id", null);
    }

    public List<User> findByRole(String role) throws MediPulseException {
        return query("SELECT " + COLS + " FROM users WHERE role=? ORDER BY name", role);
    }

    @Override
    public boolean delete(int id) throws MediPulseException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=? AND role<>'ADMIN'")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete user", e);
        }
    }

    public void updateProfile(int id, String name, String phone) throws MediPulseException {
        Validator.required(name, "Name");
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET name=?, phone=? WHERE id=?")) {
            ps.setString(1, name.trim());
            ps.setString(2, phone);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update profile", e);
        }
    }

    public void changePassword(int id, String newPassword) throws MediPulseException {
        Validator.password(newPassword);
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET password=? WHERE id=?")) {
            ps.setString(1, PasswordUtil.hash(newPassword));
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not change password", e);
        }
    }

    /** Forgot-password flow: sets a new password for a registered email. */
    public void resetPassword(String email, String newPassword) throws MediPulseException {
        String mail = Validator.email(email);
        Validator.password(newPassword);
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET password=? WHERE email=?")) {
            ps.setString(1, PasswordUtil.hash(newPassword));
            ps.setString(2, mail);
            if (ps.executeUpdate() == 0) {
                throw new ValidationException("No account found for this email");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not reset password", e);
        }
    }

    /** Number of users per role, using a LinkedHashMap to keep a fixed order. */
    public Map<String, Integer> countByRole() throws MediPulseException {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("ADMIN", 0);
        counts.put("DOCTOR", 0);
        counts.put("PATIENT", 0);
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("SELECT role, COUNT(*) FROM users GROUP BY role");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getString(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not count users", e);
        }
        return counts;
    }

    private List<User> query(String sql, String param) throws MediPulseException {
        List<User> list = new ArrayList<>();
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                ps.setString(1, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load users", e);
        }
        return list;
    }
}
