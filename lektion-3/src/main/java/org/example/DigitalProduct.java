package org.example;

import java.math.BigDecimal;

public class DigitalProduct extends ECommerceProduct implements Discountable {

    public DigitalProduct(Long id, String name, String description, BigDecimal price) {
        super(id, name, description, price);
    }

    public BigDecimal calculateShippingCost() {
        return BigDecimal.ZERO;  // Digitala produkter har ingen frakt
    }

    public BigDecimal applyDiscount() {
        BigDecimal discountRate = new BigDecimal("0.50");  // 50% rabatt
        return getPrice().subtract(getPrice().multiply(discountRate));
    }
}
