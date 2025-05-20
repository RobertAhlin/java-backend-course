package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionExample {

    public void transferStock(int sourceProductId, int targetProductId, int quantity) throws SQLException {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Starta transaktion

            // 1. Minska lagersaldo för källprodukt
            String decreaseSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ? AND stock_quantity >= ?";
            try (PreparedStatement decreaseStmt = conn.prepareStatement(decreaseSql)) {
                decreaseStmt.setInt(1, quantity);
                decreaseStmt.setInt(2, sourceProductId);
                decreaseStmt.setInt(3, quantity);

                int affected = decreaseStmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    throw new SQLException("❌ Otillräckligt lager i produkt ID: " + sourceProductId);
                }
            }

            // 2. Öka lagersaldo för målprodukt
            String increaseSql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
            try (PreparedStatement increaseStmt = conn.prepareStatement(increaseSql)) {
                increaseStmt.setInt(1, quantity);
                increaseStmt.setInt(2, targetProductId);
                increaseStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Lagerflytt lyckades!");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("↩️ Transaktion rullades tillbaka.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 🔍 Testmetod
    public static void main(String[] args) {
        TransactionExample example = new TransactionExample();
        try {
            // Flytta 3 enheter från produkt 1 till produkt 2
            example.transferStock(1, 2, 3);
        } catch (SQLException e) {
            System.err.println("💥 Fel vid lagerflytt: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
