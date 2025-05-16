package org.example.exception;

public class ProductNotFoundException extends ECommerceException {
    private final Long productId;

    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId, "PRODUCT_NOT_FOUND");
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
