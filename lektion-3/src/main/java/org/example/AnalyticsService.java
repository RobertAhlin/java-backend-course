package org.example;

public class AnalyticsService implements OrderObserver {

    @Override
    public void orderPlaced(Order order) {
        System.out.println("[AnalyticsService] Registrerar försäljningsdata för order: " + order.getOrderId());
    }
}
