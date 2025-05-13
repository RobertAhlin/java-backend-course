package com.example.catalog;

import java.util.*;

public class ProductCatalog {
    private Map<Long, Product> productsById;
    private Map<String, Set<Product>> productsByCategory;

    public ProductCatalog() {
        this.productsById = new HashMap<>();
        this.productsByCategory = new HashMap<>();
    }

    // Lägg till en produkt
    public void addProduct(Product product) {
        productsById.put(product.getId(), product);

        String category = product.getCategory();
        productsByCategory.computeIfAbsent(category, k -> new HashSet<>()).add(product);
    }

    // Hämta produkt via ID
    public Product getProductById(Long id) {
        return productsById.get(id);
    }

    // Hämta alla produkter i en viss kategori
    public Set<Product> getProductsByCategory(String category) {
        return productsByCategory.getOrDefault(category, Collections.emptySet());
    }

    // Ta bort produkt via ID
    public void removeProduct(Long id) {
        Product product = productsById.remove(id);
        if (product != null) {
            Set<Product> categoryProducts = productsByCategory.get(product.getCategory());
            if (categoryProducts != null) {
                categoryProducts.remove(product);
                if (categoryProducts.isEmpty()) {
                    productsByCategory.remove(product.getCategory());
                }
            }
        }
    }

    // Uppdatera produkt (ändra info eller kategori)
    public void updateProduct(Product updatedProduct) {
        Long id = updatedProduct.getId();
        Product existingProduct = productsById.get(id);

        if (existingProduct != null) {
            String oldCategory = existingProduct.getCategory();
            String newCategory = updatedProduct.getCategory();

            if (!oldCategory.equals(newCategory)) {
                Set<Product> oldCategoryProducts = productsByCategory.get(oldCategory);
                if (oldCategoryProducts != null) {
                    oldCategoryProducts.remove(existingProduct);
                    if (oldCategoryProducts.isEmpty()) {
                        productsByCategory.remove(oldCategory);
                    }
                }

                productsByCategory.computeIfAbsent(newCategory, k -> new HashSet<>()).add(updatedProduct);
            }

            productsById.put(id, updatedProduct);
        }
    }

    // Sök efter produkter via namn (substring-match)
    public List<Product> searchByName(String keyword) {
        List<Product> results = new ArrayList<>();
        for (Product product : productsById.values()) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    // Hämta alla unika kategorier
    public Set<String> getAllCategories() {
        return productsByCategory.keySet();
    }

    // Räkna alla produkter totalt
    public int getTotalProductCount() {
        return productsById.size();
    }
    public Collection<Product> getAllProducts() {
        return productsById.values();
    }
}
