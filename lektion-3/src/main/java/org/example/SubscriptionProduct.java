package org.example;

import java.math.BigDecimal;

public class SubscriptionProduct extends ECommerceProduct implements Featured {

    public SubscriptionProduct(Long id, String name, String description, BigDecimal price) {
        super(id, name, description, price);
    }

    public BigDecimal calculateShippingCost() {
        // Vi säger att abonnemang alltid har 100 kr startfrakt
        return new BigDecimal("100.0");
    }

    public String getFeaturedMessage() {
        return "🔥 Månadens produkt: " + getName();
    }
}
