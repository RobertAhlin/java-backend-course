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

        try {
            Address address = new Address("Storgatan 1", "Stockholm", "12345");
            service.validateAddress(address);
            System.out.println("Valid address passed.");
        } catch (ValidationException e) {
            System.out.println("Address validation failed: " + e.getMessage());
        }
    }
}
