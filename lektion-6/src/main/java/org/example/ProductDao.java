package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {

    // Hämta alla produkter
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (
                Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }

        return products;
    }

    // Hämta produkt med ID
    public Optional<Product> findById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Spara (insert eller update)
    public Product save(Product product) throws SQLException {
        if (product.getId() == 0) {
            return insert(product);
        } else {
            return update(product);
        }
    }

    // Infoga ny produkt
    private Product insert(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, category_id) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setInt(5, product.getCategoryId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
        }

        return product;
    }

    // Uppdatera befintlig produkt
    private Product update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, category_id = ? WHERE product_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setInt(5, product.getCategoryId());
            pstmt.setInt(6, product.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating product failed, no rows affected.");
            }
        }

        return product;
    }

    // Radera produkt
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hämta produkter i viss kategori
    public List<Product> findByCategory(int categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }

        return products;
    }

    // 🔍 Avancerade metoder 🔍

    // 1. Produkter inom ett prisintervall
    public List<Product> findByPriceRange(double min, double max) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setDouble(1, min);
            pstmt.setDouble(2, max);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }

        return products;
    }

    // 2. Produkter med lågt lagersaldo
    public List<Product> findLowStockProducts(int threshold) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE stock_quantity < ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, threshold);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }

        return products;
    }

    // 3. Produkter med total försäljning
    public List<String> getProductsWithSales() throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = """
            SELECT p.name, SUM(oi.quantity) AS total_sold
            FROM products p
            JOIN order_items oi ON p.product_id = oi.product_id
            GROUP BY p.product_id, p.name
            ORDER BY total_sold DESC
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                String line = rs.getString("name") + " - Sålda enheter: " + rs.getInt("total_sold");
                result.add(line);
            }
        }

        return result;
    }

    // 4. Uppdatera kategori för produkter
    public int updateCategoryForProducts(String oldCategoryName, String newCategoryName) throws SQLException {
        String getCategoryIdSql = "SELECT category_id FROM categories WHERE name = ?";
        Integer oldId = null, newId = null;

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(getCategoryIdSql)) {
                stmt.setString(1, oldCategoryName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) oldId = rs.getInt("category_id");
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement(getCategoryIdSql)) {
                stmt.setString(1, newCategoryName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) newId = rs.getInt("category_id");
                }
            }

            if (oldId == null || newId == null) {
                throw new SQLException("En eller båda kategorierna hittades inte.");
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE products SET category_id = ? WHERE category_id = ?")) {
                updateStmt.setInt(1, newId);
                updateStmt.setInt(2, oldId);
                return updateStmt.executeUpdate();
            }
        }
    }

    // 5. Sök produkter efter namn
    public List<Product> searchProductsByName(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }

        return products;
    }

    // 📦 Hjälpmetod: omvandla ResultSet till Product
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setCategoryId(rs.getInt("category_id"));
        return product;
    }
}
