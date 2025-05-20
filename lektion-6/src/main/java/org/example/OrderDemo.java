package org.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderDemo {
    public static void main(String[] args) {
        OrderDao orderDao = new OrderDao();

        try {
            // 🔹 1. Skapa en ny order (byt till giltiga ID:n för user + adresser!)
            Order newOrder = new Order(
                    1,              // user_id
                    499.90,         // total_amount
                    Order.Status.PENDING,
                    1,              // shipping_address_id
                    2               // billing_address_id
            );
            Order savedOrder = orderDao.save(newOrder);
            System.out.println("✅ Skapad order: " + savedOrder);

            // 🔹 2. Hämta alla orders
            System.out.println("\n📋 Alla beställningar:");
            List<Order> orders = orderDao.findAll();
            orders.forEach(System.out::println);

            // 🔹 3. Uppdatera orderstatus
            savedOrder.setStatus(Order.Status.SHIPPED);
            orderDao.save(savedOrder);
            System.out.println("\n✏️ Uppdaterad order: " + savedOrder);

            // 🔹 4. Hämta order med ID
            Optional<Order> found = orderDao.findById(savedOrder.getId());
            found.ifPresent(o -> System.out.println("\n🔍 Hittad order: " + o));

            // 🔹 5. Ta bort order
            boolean deleted = orderDao.delete(savedOrder.getId());
            System.out.println("\n🗑️ Order raderad: " + deleted);

        } catch (SQLException e) {
            System.err.println("💥 Fel: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
