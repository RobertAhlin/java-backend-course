package org.example;

public class BankTransferPaymentStrategy implements PaymentStrategy {

    public void processPayment(double amount) {
        System.out.println("Betalar " + amount + " kr via banköverföring.");
        // Här skulle du normalt hantera banköverföring
    }
}
