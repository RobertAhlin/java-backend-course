package org.example;

import java.math.BigDecimal;

public abstract class ECommerceProduct {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    public ECommerceProduct(Long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDisplayInfo() {
        return name + " - " + price + " kr";
    }

    public abstract BigDecimal calculateShippingCost();
}
