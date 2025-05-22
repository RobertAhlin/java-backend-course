package com.example.product_api.config;

import com.example.product_api.model.Product;
import com.example.product_api.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(new Product(null, "Laptop", 12999.0, "Kraftfull laptop", "electronics"));
            productRepository.save(new Product(null, "Mobiltelefon", 7999.0, "Ny smartphone", "electronics"));
            productRepository.save(new Product(null, "T-shirt", 199.0, "Bomullströja", "clothing"));
            productRepository.save(new Product(null, "Kaffemugg", 79.0, "Keramikkopp", "kitchen"));
            productRepository.save(new Product(null, "Hörlurar", 499.0, "Bluetooth-headset", "electronics"));
        }
    }
}
