package org.example;

@FunctionalInterface
public interface PriceCalculator {
    double calculate(Product product);
}
