package org.example.repository;

import org.example.model.Product;

public interface ProductRepository {
    Product findById(Long id);
    void save(Product product);
}
