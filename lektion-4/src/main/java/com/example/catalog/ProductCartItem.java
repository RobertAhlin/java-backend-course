package com.example.catalog;

public class ProductCartItem implements CartItem {
    private Product product;
    private int quantity;

    public ProductCartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return product.getName();
    }

    @Override
    public double getPrice() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return quantity + " x " + product.getName() + " (" + product.getCategory() + ") - " + getPrice() + " kr";
    }
}
