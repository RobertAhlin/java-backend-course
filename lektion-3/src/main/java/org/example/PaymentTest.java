package org.example;

public class PaymentTest {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor(new CreditCardPaymentStrategy());

        // Simulerar en beställning
        System.out.println("Beställning #1:");
        processor.pay(999.0);  // Betala med kreditkort

        // Byter strategi till PayPal
        System.out.println("\nBeställning #2:");
        processor.setPaymentStrategy(new PayPalPaymentStrategy());
        processor.pay(499.0);  // Betala med PayPal

        // Byter strategi till Banköverföring
        System.out.println("\nBeställning #3:");
        processor.setPaymentStrategy(new BankTransferPaymentStrategy());
        processor.pay(1499.0);  // Betala med banköverföring
    }
}
