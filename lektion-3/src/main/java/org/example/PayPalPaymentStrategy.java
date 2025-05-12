package org.example;

public class PayPalPaymentStrategy implements PaymentStrategy {

    public void processPayment(double amount) {
        System.out.println("Betalar " + amount + " kr via PayPal.");
        // Här skulle du normalt integrera PayPal API
    }
}
