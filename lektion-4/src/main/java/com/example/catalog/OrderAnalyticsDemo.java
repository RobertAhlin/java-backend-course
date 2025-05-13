package com.example.catalog;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderAnalyticsDemo {
    public static void main(String[] args) {
        // Skapa produkter
        Product laptop = new Product(1L, "Laptop", 9999.0, "Elektronik", 10);
        Product mouse = new Product(2L, "Musmatta", 199.0, "Elektronik", 50);
        Product shampoo = new Product(3L, "Shampoo", 59.90, "Hygien", 100);
        Product phone = new Product(4L, "Mobiltelefon", 5999.0, "Elektronik", 20);
        Product toothbrush = new Product(5L, "Tandborste", 29.90, "Hygien", 200);

        // Skapa orders
        Order order1 = new Order(1001L, "customer1", LocalDate.of(2025, Month.JANUARY, 10),
                Arrays.asList(new OrderItem(laptop, 1), new OrderItem(mouse, 2)));

        Order order2 = new Order(1002L, "customer2", LocalDate.of(2025, Month.JANUARY, 15),
                Arrays.asList(new OrderItem(shampoo, 3), new OrderItem(toothbrush, 5)));

        Order order3 = new Order(1003L, "customer1", LocalDate.of(2025, Month.FEBRUARY, 5),
                Arrays.asList(new OrderItem(phone, 1), new OrderItem(mouse, 1)));

        Order order4 = new Order(1004L, "customer3", LocalDate.of(2025, Month.FEBRUARY, 20),
                Arrays.asList(new OrderItem(laptop, 2), new OrderItem(shampoo, 1)));

        // Lägg alla orders i lista
        List<Order> orders = Arrays.asList(order1, order2, order3, order4);

        // Skapa OrderAnalytics
        OrderAnalytics analytics = new OrderAnalytics(orders);

        // 1. Försäljning per månad
        System.out.println("Försäljning per månad:");
        analytics.salesPerMonth().forEach((month, sales) ->
                System.out.println(month + ": " + sales + " kr"));

        // 2. Mest sålda produkter
        System.out.println("\nMest sålda produkter:");
        analytics.mostSoldProducts().forEach((product, qty) ->
                System.out.println(product.getName() + ": " + qty + " sålda"));

        // 3. Köpmönster per kund
        System.out.println("\nKöpmönster per kund:");
        analytics.customerPurchasePatterns().forEach((customerId, count) ->
                System.out.println(customerId + ": " + count + " orders"));

        // 4. Kunder som köpte X, köpte även Y
        System.out.println("\nKunder som köpte X, köpte även Y:");
        analytics.alsoBought().forEach((product, others) -> {
            System.out.print(product.getName() + " → ");
            others.forEach(p -> System.out.print(p.getName() + " "));
            System.out.println();
        });

        // 5. Populära produkter per kategori
        System.out.println("\nPopulära produkter per kategori:");
        analytics.popularProductsByCategory().forEach((category, productsInCat) -> {
            System.out.println(category + ":");
            productsInCat.forEach(p -> System.out.println("  - " + p.getName()));
        });

        // 6. Avancerad produktsökning (alla produkter med pris > 1000)
        System.out.println("\nProdukter med pris > 1000 kr:");
        analytics.advancedProductSearch(p -> p.getPrice() > 1000)
                .forEach(p -> System.out.println(p.getName()));
    }
}
