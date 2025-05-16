package org.example;

public class ValidationService {

    public void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new ValidationException("Invalid email format: " + email);
        }
    }

    public void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasLetter && hasDigit) {
                break;
            }
        }

        if (!hasLetter || !hasDigit) {
            throw new ValidationException("Password must contain at least one letter and one digit");
        }
    }

    public void validateProduct(Product product) throws ValidationException {
        if (product == null) {
            throw new ValidationException("Product cannot be null");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ValidationException("Product name cannot be empty");
        }

        if (product.getPrice() <= 0) {
            throw new ValidationException("Product price must be positive");
        }

        if (product.getStockQuantity() < 0) {
            throw new ValidationException("Product stock quantity cannot be negative");
        }
    }

    public void validateAddress(Address address) throws AddressValidationException {
        if (address == null) {
            throw new AddressValidationException("Address cannot be null");
        }

        if (address.getStreet() == null || address.getStreet().isBlank()) {
            throw new AddressValidationException("Street cannot be empty");
        }

        if (address.getCity() == null || address.getCity().isBlank()) {
            throw new AddressValidationException("City cannot be empty");
        }

        if (address.getPostalCode() == null || !address.getPostalCode().matches("\\d{5}")) {
            throw new AddressValidationException("Postal code must be 5 digits");
        }
    }


}
