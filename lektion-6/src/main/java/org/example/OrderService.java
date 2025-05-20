package org.example;

import java.sql.*;
import java.util.Map;

public class OrderService {

    /**
     * Skapar en ny order med tillhörande orderrader, uppdaterar lager,
     * och kör allt som en transaktion.
     *
     * @param customerId ID för kunden
     * @param items En map där key = productId och value = quantity
     */
    public void createOrder(int customerId, Map<Integer, Integer> items) throws SQLException {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Starta transaktion

            // 1. Räkna ut totalbelopp
            double totalAmount = 0;
            for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();

                try (PreparedStatement stmt = conn.prepareStatement("SELECT price, stock_quantity FROM products WHERE product_id = ?")) {
                    stmt.setInt(1, productId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            double price = rs.getDouble("price");
                            int stock = rs.getInt("stock_quantity");
                            if (stock < quantity) {
                                conn.rollback();
                                throw new SQLException("❌ Otillräckligt lager för produkt ID: " + productId);
                            }
                            totalAmount += price * quantity;
                        } else {
                            conn.rollback();
                            throw new SQLException("❌ Produkt hittades inte: ID " + productId);
                        }
                    }
                }
            }

            // 2. Skapa order
            int orderId;
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO orders (customer_id, total_amount, status) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, customerId);
                stmt.setDouble(2, totalAmount);
                stmt.setString(3, "PROCESSING");

                stmt.executeUpdate();
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        orderId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        throw new SQLException("❌ Kunde inte skapa order");
                    }
                }
            }

            // 3. Lägg till order_items och uppdatera lager
            for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();

                // a) Lägg till orderrad
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) " +
                                "VALUES (?, ?, ?, (SELECT price FROM products WHERE product_id = ?))")) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, productId);
                    stmt.setInt(3, quantity);
                    stmt.setInt(4, productId);
                    stmt.executeUpdate();
                }

                // b) Minska lager
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?")) {
                    stmt.setInt(1, quantity);
                    stmt.setInt(2, productId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("✅ Order skapad med ID: " + orderId + ", total: " + totalAmount);

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
                System.out.println("↩️ Transaktion rullades tillbaka p.g.a. fel: " + e.getMessage());
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    // 🧪 Testa beställningsprocess
    public static void main(String[] args) {
        OrderService service = new OrderService();

        // T.ex. köp 1 st av produkt 1 och 2 st av produkt 2
        Map<Integer, Integer> items = Map.of(
                1, 1,
                2, 2
        );

        try {
            service.createOrder(1, items); // byt till en giltig customer_id
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
