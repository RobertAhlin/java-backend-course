package org.example;

import java.math.BigDecimal;

public class ProductTest {
    public static void main(String[] args) {
        PhysicalProduct laptop = new PhysicalProduct(1L, "Laptop", "Gaminglaptop", new BigDecimal("15000"), 2.5);

        System.out.println(laptop.getDisplayInfo());
        System.out.println("Fraktkostnad: " + laptop.calculateShippingCost() + " kr");

        DigitalProduct ebook = new DigitalProduct(2L, "E-bok", "Java för nybörjare", new BigDecimal("299"));

        System.out.println(ebook.getDisplayInfo());
        System.out.println("Fraktkostnad: " + ebook.calculateShippingCost() + " kr");

        SubscriptionProduct magazine = new SubscriptionProduct(3L, "Månadstidning", "Java Monthly", new BigDecimal("99"));

        System.out.println(magazine.getDisplayInfo());
        System.out.println("Fraktkostnad: " + magazine.calculateShippingCost() + " kr");

        System.out.println("Moms: " + laptop.calculateTax() + " kr");
        System.out.println("Pris efter rabatt: " + laptop.applyDiscount() + " kr");

        System.out.println(magazine.getFeaturedMessage());

        System.out.println("Rabatterat pris: " + ebook.applyDiscount() + " kr");

    }


}
