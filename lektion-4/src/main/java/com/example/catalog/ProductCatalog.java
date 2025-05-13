package com.example.catalog;

import java.util.*;
import java.util.stream.Collectors;

public class ProductCatalog extends InMemoryRepository<Product, Long> {
    private final Map<String, Set<Product>> productsByCategory = new HashMap<>();

    public ProductCatalog() {
        super(Product::getId);
    }

    @Override
    public Product save(Product product) {
        Product savedProduct = super.save(product);

        productsByCategory
                .computeIfAbsent(product.getCategory(), k -> new HashSet<>())
                .add(product);

        return savedProduct;
    }

    @Override
    public void delete(Product product) {
        super.delete(product);

        Set<Product> categorySet = productsByCategory.get(product.getCategory());
        if (categorySet != null) {
            categorySet.remove(product);
            if (categorySet.isEmpty()) {
                productsByCategory.remove(product.getCategory());
            }
        }
    }

    public Set<String> getAllCategories() {
        return productsByCategory.keySet();
    }

    public Set<Product> getProductsByCategory(String category) {
        return productsByCategory.getOrDefault(category, Collections.emptySet());
    }

    public List<Product> searchByName(String keyword) {
        return findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
