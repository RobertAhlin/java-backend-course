package org.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductDemo {
    public static void main(String[] args) {
        ProductDao dao = new ProductDao();

        try {
            // 🔹 1. Skapa ny produkt
            Product newProduct = new Product(
                    "Demo Keyboard",
                    "Testprodukt för JDBC",
                    199.99,
                    10,
                    1 // byt till en giltig category_id från din databas!
            );
            Product savedProduct = dao.save(newProduct);
            System.out.println("✅ Produkt skapad: " + savedProduct);

            // 🔹 2. Hämta alla produkter
            List<Product> allProducts = dao.findAll();
            System.out.println("📦 Alla produkter:");
            allProducts.forEach(System.out::println);

            // 🔹 3. Uppdatera produkt
            savedProduct.setPrice(149.99);
            savedProduct.setStockQuantity(20);
            dao.save(savedProduct);
            System.out.println("✏️ Uppdaterad produkt: " + savedProduct);

            // 🔹 4. Hämta produkt med ID
            Optional<Product> found = dao.findById(savedProduct.getId());
            found.ifPresent(p -> System.out.println("🔍 Hittad: " + p));

            // 🔹 5. Ta bort produkt
            boolean deleted = dao.delete(savedProduct.getId());
            System.out.println(deleted ? "🗑️ Produkt raderad" : "❌ Kunde inte radera");

        } catch (SQLException e) {
            System.err.println("💥 Fel: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
