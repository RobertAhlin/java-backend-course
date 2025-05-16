package org.example.repository;

import org.example.model.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> productMap = new ConcurrentHashMap<>();

    public InMemoryProductRepository() {
        // Lägg till exempeldata
        productMap.put(1L, new Product(1L, "TV", 5));
        productMap.put(2L, new Product(2L, "Laptop", 10));
    }

    @Override
    public Product findById(Long id) {
        return productMap.get(id);
    }

    @Override
    public void save(Product product) {
        productMap.put(product.getId(), product);
    }
}
