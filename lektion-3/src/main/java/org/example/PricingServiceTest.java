package org.example;

public class PricingServiceTest {
    public static void main(String[] args) {
        Product laptop = new Product(1, "Laptop", 1200.0, "Electronics");

        PricingService service = new PricingService();

        // --- Prisuträkningar ---
        PriceCalculator standardPrice = product -> product.getPrice();
        PriceCalculator memberDiscount = product -> product.getPrice() * 0.9;  // 10% rabatt
        PriceCalculator salePrice = product -> product.getPrice() * 0.75;      // 25% rabatt

        System.out.println("Standardpris: " + service.calculateFinalPrice(laptop, standardPrice));
        System.out.println("Medlemspris: " + service.calculateFinalPrice(laptop, memberDiscount));
        System.out.println("Rea-pris: " + service.calculateFinalPrice(laptop, salePrice));

        // --- Valideringar ---
        ProductValidator expensiveValidator = p -> p.getPrice() > 500;
        ProductValidator electronicsValidator = p -> p.getCategory().equalsIgnoreCase("Electronics");
        ProductValidator fakeStockValidator = p -> true;  // (låtsas alltid finnas i lager)

        System.out.println("Är dyr? " + service.isValid(laptop, expensiveValidator));
        System.out.println("Är elektronik? " + service.isValid(laptop, electronicsValidator));
        System.out.println("Finns i lager? " + service.isValid(laptop, fakeStockValidator));
    }
}
