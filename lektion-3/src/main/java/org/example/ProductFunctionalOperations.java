package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProductFunctionalOperations {

    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();

        // Lägg till produkter
        products.add(new Product(1, "Laptop", 1200.0, "Electronics"));
        products.add(new Product(2, "Phone", 800.0, "Electronics"));
        products.add(new Product(3, "Desk", 350.0, "Furniture"));
        products.add(new Product(4, "Book", 15.0, "Books"));
        products.add(new Product(5, "Tablet", 600.0, "Electronics"));

        System.out.println("Alla produkter:");
        processProducts(products, p -> System.out.println(p));

        List<String> productNames = transformProducts(products, Product::getName);

        System.out.println("Produktnamn:");
        for (String name : productNames) {
            System.out.println(name);
        }

        //Testar priskalkulatorn
        PriceCalculator taxCalculator = product -> product.getPrice() * 1.25;
        Product product = new Product(1, "Laptop", 1000.0, "Electronics");

        double finalPrice = taxCalculator.calculate(product);
        System.out.println("Slutpris med moms: " + finalPrice);

        //Testa ProductValidator
        ProductValidator expensiveValidator = p -> p.getPrice() > 500;
        boolean isExpensive = expensiveValidator.validate(product);

        System.out.println("Är produkten dyr? " + isExpensive);

    }

    // Steg 2: filterProducts-metod hit
    public static List<Product> filterProducts(List<Product> products, Predicate<Product> predicate) {
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (predicate.test(product)) {
                filtered.add(product);
            }
        }
        return filtered;
    }
    // Steg 3:
    public static void processProducts(List<Product> products, Consumer<Product> consumer) {
        for (Product product : products) {
            consumer.accept(product);
        }
    }

    //Steg 4
    public static <R> List<R> transformProducts(List<Product> products, Function<Product, R> function) {
        List<R> results = new ArrayList<>();
        for (Product product : products) {
            results.add(function.apply(product));
        }
        return results;
    }




}
