package org.example.exception;

public class InsufficientStockException extends ECommerceException {
    private final Long productId;
    private final int requestedQuantity;
    private final int availableQuantity;

    public InsufficientStockException(Long productId, int requestedQuantity, int availableQuantity) {
        super(
                String.format("Insufficient stock for product %d. Requested: %d, Available: %d",
                        productId, requestedQuantity, availableQuantity),
                "INSUFFICIENT_STOCK"
        );
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
