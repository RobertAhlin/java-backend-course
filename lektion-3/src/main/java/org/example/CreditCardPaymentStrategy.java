package org.example;

public class CreditCardPaymentStrategy implements PaymentStrategy {

    public void processPayment(double amount) {
        System.out.println("Betalar " + amount + " kr med kreditkort.");
        // Här skulle du normalt anropa ett kreditkorts-API
    }
}
