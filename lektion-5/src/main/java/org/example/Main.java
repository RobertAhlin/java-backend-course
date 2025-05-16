package org.example;

public class Main {
    public static void main(String[] args) {
        ValidationService service = new ValidationService();

        try {
            service.validateEmail("invalid-email");
        } catch (ValidationException e) {
            System.out.println("Email validation failed: " + e.getMessage());
        }

        try {
            service.validatePassword("123");
        } catch (ValidationException e) {
            System.out.println("Password validation failed: " + e.getMessage());
        }

        try {
            Product p = new Product("TV", 0, 5);
            service.validateProduct(p);
        } catch (ValidationException e) {
            System.out.println("Product validation failed: " + e.getMessage());
        }
    }
}
