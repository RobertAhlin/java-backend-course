package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao implements Repository<Order, Integer> {

    @Override
    public Optional<Order> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrder(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        }

        return orders;
    }

    @Override
    public Order save(Order order) throws SQLException {
        if (order.getId() == 0) {
            return insert(order);
        } else {
            return update(order);
        }
    }

    private Order insert(Order order) throws SQLException {
        String sql = """
            INSERT INTO orders (user_id, total_amount, status, shipping_address_id, billing_address_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getStatus().name());
            stmt.setInt(4, order.getShippingAddressId());
            stmt.setInt(5, order.getBillingAddressId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Kunde inte skapa order.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setId(keys.getInt(1));
                }
            }
        }

        return order;
    }

    private Order update(Order order) throws SQLException {
        String sql = """
            UPDATE orders SET user_id = ?, total_amount = ?, status = ?, 
            shipping_address_id = ?, billing_address_id = ? WHERE order_id = ?
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getStatus().name());
            stmt.setInt(4, order.getShippingAddressId());
            stmt.setInt(5, order.getBillingAddressId());
            stmt.setInt(6, order.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Kunde inte uppdatera order.");
            }
        }

        return order;
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(Order.Status.valueOf(rs.getString("status")));
        order.setShippingAddressId(rs.getInt("shipping_address_id"));
        order.setBillingAddressId(rs.getInt("billing_address_id"));
        return order;
    }
}
