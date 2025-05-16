package org.example.service;

import org.example.exception.InsufficientStockException;
import org.example.exception.ProductNotFoundException;
import org.example.logging.AppLogger;
import org.example.model.Product;
import org.example.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private final ProductRepository productRepository;
    private final AppLogger logger;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.logger = new AppLogger(ProductService.class);
    }

    public Product getProduct(Long id) throws ProductNotFoundException {
        logger.info("Getting product with ID: {}", id);

        Product product = productRepository.findById(id);
        if (product == null) {
            logger.warn("Product not found with ID: {}", id);
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    public void updateStock(Long productId, int quantity)
            throws ProductNotFoundException, InsufficientStockException {

        Map<String, Object> context = new HashMap<>();
        context.put("productId", productId);
        context.put("quantity", quantity);

        logger.infoWithContext("Updating stock", context);

        Product product = getProduct(productId);

        if (quantity < 0 && Math.abs(quantity) > product.getStockQuantity()) {
            throw new InsufficientStockException(
                    productId, Math.abs(quantity), product.getStockQuantity());
        }

        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        logger.info("Stock updated for product {}, new quantity: {}",
                productId, product.getStockQuantity());
    }
}
