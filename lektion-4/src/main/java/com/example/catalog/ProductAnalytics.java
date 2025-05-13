package com.example.catalog;

import java.util.*;
import java.util.stream.Collectors;

public class ProductAnalytics {
    private List<Product> products;

    public ProductAnalytics(List<Product> products) {
        this.products = new ArrayList<>(products);
    }

    // Hitta produkter inom ett prisintervall
    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Hitta de dyraste produkterna (topp N)
    public List<Product> findTopNExpensiveProducts(int n) {
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // Gruppera produkter efter kategori
    public Map<String, List<Product>> groupByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
    }

    // Beräkna genomsnittspris per kategori
    public Map<String, Double> calculateAveragePriceByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));
    }

    // Hitta produkter med lågt lagersaldo
    public List<Product> findLowStockProducts(int threshold) {
        return products.stream()
                .filter(p -> p.getStockQuantity() < threshold)
                .collect(Collectors.toList());
    }

    // Beräkna totalt lagervärde
    public double calculateTotalInventoryValue() {
        return products.stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
    }

    // Hitta produktnamn som matchar en sökfras
    public List<String> searchProductNames(String searchTerm) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(Product::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    // Skapa en sammanfattning av produktstatistik
    public String generateProductSummary() {
        DoubleSummaryStatistics stats = products.stream()
                .collect(Collectors.summarizingDouble(Product::getPrice));

        return String.format(
                "Product Summary:%n" +
                        "Total Products: %d%n" +
                        "Average Price: $%.2f%n" +
                        "Lowest Price: $%.2f%n" +
                        "Highest Price: $%.2f%n" +
                        "Total Categories: %d",
                stats.getCount(),
                stats.getAverage(),
                stats.getMin(),
                stats.getMax(),
                products.stream().map(Product::getCategory).distinct().count()
        );
    }
}