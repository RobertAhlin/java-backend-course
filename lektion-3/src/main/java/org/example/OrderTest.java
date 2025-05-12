package org.example;

public class OrderTest {
    public static void main(String[] args) {
        // Skapa OrderService (subject)
        OrderService orderService = new OrderService();

        // Registrera observers
        orderService.addObserver(new InventorySystem());
        orderService.addObserver(new NotificationService());
        orderService.addObserver(new AnalyticsService());

        // Skapa en order
        Order order = new Order("ORD123", "customer@example.com");

        // Lägg ordern (triggar observer-meddelande)
        orderService.placeOrder(order);
    }
}
