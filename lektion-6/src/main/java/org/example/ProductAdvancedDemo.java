package org.example;

import java.sql.SQLException;
import java.util.List;

public class ProductAdvancedDemo {
    public static void main(String[] args) {
        ProductDao dao = new ProductDao();

        try {
            // 🔹 1. Produkter inom prisintervall
            System.out.println("🔍 Produkter mellan 100 och 1000 kr:");
            List<Product> priceRange = dao.findByPriceRange(100, 1000);
            priceRange.forEach(System.out::println);

            // 🔹 2. Produkter med lågt lagersaldo
            System.out.println("\n⚠️ Produkter med lagersaldo under 10:");
            List<Product> lowStock = dao.findLowStockProducts(10);
            lowStock.forEach(System.out::println);

            // 🔹 3. Försäljningsstatistik
            System.out.println("\n📈 Produkter med försäljningsdata:");
            List<String> sales = dao.getProductsWithSales();
            sales.forEach(System.out::println);

            // 🔹 4. Uppdatera kategori
            System.out.println("\n🛠️ Uppdatera kategori från 'Electronics' till 'Tech':");
            int updated = dao.updateCategoryForProducts("Electronics", "Tech");
            System.out.println("Antal uppdaterade produkter: " + updated);

            // 🔹 5. Sök produkter med namn som innehåller "book"
            System.out.println("\n🔎 Sökning: produkter som innehåller 'book':");
            List<Product> search = dao.searchProductsByName("book");
            search.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("💥 Fel: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
