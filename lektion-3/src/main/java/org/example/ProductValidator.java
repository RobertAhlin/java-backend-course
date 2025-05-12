package org.example;

@FunctionalInterface
public interface ProductValidator {
    boolean validate(Product product);
}
