package com.example.catalog;

import java.util.Comparator;
import java.util.List;

public class ProductCatalogDemo {
    public static void main(String[] args) {
        ProductCatalog catalog = new ProductCatalog();

        // Lägg till produkter
        catalog.addProduct(new Product(1L, "Tandborste", 29.90, "Hygien", 100));
        catalog.addProduct(new Product(2L, "Shampoo", 59.90, "Hygien", 50));
        catalog.addProduct(new Product(3L, "Laptop", 9999.0, "Elektronik", 10));
        catalog.addProduct(new Product(4L, "Musmatta", 199.0, "Elektronik", 30));
        catalog.addProduct(new Product(5L, "Mobiltelefon", 5999.0, "Elektronik", 20));

        // 🔎 Visa alla produkter (alla från ID-map)
        System.out.println("Alla produkter:");
        catalog.getTotalProductCount(); // Bara för test
        catalog.getAllCategories().forEach(cat ->
                catalog.getProductsByCategory(cat).forEach(System.out::println));

        // 🔎 Hämta en produkt via ID
        System.out.println("\nProdukt med ID 2:");
        System.out.println(catalog.getProductById(2L));

        // 🔎 Sök produkter via namn
        System.out.println("\nSök efter 'Lap':");
        catalog.searchByName("Lap").forEach(System.out::println);

        // 🔎 Visa produkter per kategori
        System.out.println("\nAntal produkter per kategori:");
        catalog.getAllCategories().forEach(category -> {
            int count = catalog.getProductsByCategory(category).size();
            System.out.println(category + ": " + count);
        });

        // 🔎 Sortera produkter efter pris (stigande)
        System.out.println("\nProdukter sorterade efter pris (stigande):");
        catalog.getAllProducts().stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .forEach(System.out::println);

        // 🔎 Filtrera produkter inom prisintervall
        System.out.println("\nProdukter mellan 50 och 6000 kr:");
        catalog.getAllProducts().stream()
                .filter(p -> p.getPrice() >= 50 && p.getPrice() <= 6000)
                .forEach(System.out::println);

        // 🔎 Lägg till recensioner
        Product laptop = catalog.getProductById(3L);
        laptop.addReview("Erik", "Bra prestanda!");
        laptop.addReview("Sara", "Lite dyr, men värd pengarna");

        System.out.println("\nRecensioner för Laptop:");
        laptop.getReviews().forEach((user, comment) ->
                System.out.println(user + ": " + comment));
    }
}
