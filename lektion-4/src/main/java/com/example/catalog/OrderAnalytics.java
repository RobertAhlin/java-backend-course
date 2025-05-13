package com.example.catalog;

import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderAnalytics {
    private List<Order> orders;

    public OrderAnalytics(List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }

    // 1. Försäljning per månad
    public Map<Month, Double> salesPerMonth() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getOrderDate().getMonth(),
                        Collectors.summingDouble(order -> order.getItems().stream()
                                .mapToDouble(OrderItem::getTotalPrice).sum())
                ));
    }

    // 2. Mest sålda produkter (produkt -> antal sålda)
    public Map<Product, Integer> mostSoldProducts() {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingInt(OrderItem::getQuantity)
                ));
    }

    // 3. Köpmönster per kund (kund -> totalt antal köp)
    public Map<String, Long> customerPurchasePatterns() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomerId,
                        Collectors.counting()
                ));
    }

    // 4. "Kunder som köpte X, köpte även Y"
    public Map<Product, Set<Product>> alsoBought() {
        Map<Product, Set<Product>> recommendations = new HashMap<>();

        orders.forEach(order -> {
            List<Product> products = order.getItems().stream()
                    .map(OrderItem::getProduct)
                    .collect(Collectors.toList());

            for (Product p : products) {
                recommendations.computeIfAbsent(p, k -> new HashSet<>()).addAll(
                        products.stream().filter(other -> !other.equals(p)).collect(Collectors.toSet())
                );
            }
        });

        return recommendations;
    }

    // 5. Populära produkter i kategori Z
    public Map<String, List<Product>> popularProductsByCategory() {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getCategory(),
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(OrderItem::getProduct, Collectors.summingInt(OrderItem::getQuantity)),
                                map -> map.entrySet().stream()
                                        .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                                        .map(Map.Entry::getKey)
                                        .collect(Collectors.toList())
                        )
                ));
    }

    // 6. Avancerad produktsökning med filter
    public List<Product> advancedProductSearch(Predicate<Product> filter) {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProduct)
                .filter(filter)
                .distinct()
                .collect(Collectors.toList());
    }
}
