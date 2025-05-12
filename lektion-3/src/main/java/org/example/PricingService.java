package org.example;

public class PricingService {

    public double calculateFinalPrice(Product product, PriceCalculator calculator) {
        return calculator.calculate(product);
    }

    public boolean isValid(Product product, ProductValidator validator) {
        return validator.validate(product);
    }
}
