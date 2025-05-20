package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Repository<User, Integer> {

    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }

        return users;
    }

    @Override
    public User save(User user) throws SQLException {
        if (user.getId() == 0) {
            return insert(user);
        } else {
            return update(user);
        }
    }

    private User insert(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password_hash, first_name, last_name, phone, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getRole().name());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
        }

        return user;
    }

    private User update(User user) throws SQLException {
        String sql = "UPDATE users SET email = ?, password_hash = ?, first_name = ?, last_name = ?, phone = ?, role = ? WHERE user_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getRole().name());
            pstmt.setInt(7, user.getId());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }

        return user;
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhone(rs.getString("phone"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
