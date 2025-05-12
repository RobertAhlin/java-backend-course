package org.example;

public class NotificationService implements OrderObserver {

    public void orderPlaced(Order order) {
        System.out.println("[NotificationService] Skickar orderbekräftelse till: " + order.getCustomerEmail());
    }
}
