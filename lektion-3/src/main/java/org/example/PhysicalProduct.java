package org.example;

import java.math.BigDecimal;

public class PhysicalProduct extends ECommerceProduct implements Taxable, Discountable {
    private double weight; // i kg

    public PhysicalProduct(Long id, String name, String description, BigDecimal price, double weight) {
        super(id, name, description, price);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public BigDecimal calculateShippingCost() {
        BigDecimal costPerKg = new BigDecimal("50.0"); // 50 kr/kg
        return costPerKg.multiply(BigDecimal.valueOf(weight));
    }

    @Override
    public BigDecimal calculateTax() {
        BigDecimal taxRate = new BigDecimal("0.25"); // 25% moms
        return getPrice().multiply(taxRate);
    }

    @Override
    public BigDecimal applyDiscount() {
        BigDecimal discountRate = new BigDecimal("0.10"); // 10% rabatt
        return getPrice().subtract(getPrice().multiply(discountRate));
    }
}
