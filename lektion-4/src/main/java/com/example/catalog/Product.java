package com.example.catalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private double price;
    private String category;
    private int stockQuantity;
    private Map<String, String> reviews = new HashMap<>();

    public Product(Long id, String name, double price, String category, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    // Recensioner
    public void addReview(String username, String comment) {
        reviews.put(username, comment);
    }

    public Map<String, String> getReviews() {
        return reviews;
    }

    // equals() & hashCode() (använder id som unik identitet)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString() för snygg utskrift
    @Override
    public String toString() {
        return name + " (" + category + ") - " + price + "kr, Lager: " + stockQuantity;
    }
}
