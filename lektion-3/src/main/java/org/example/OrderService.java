package org.example;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void placeOrder(Order order) {
        System.out.println("Order lagd: " + order.getOrderId());

        // Notifiera alla observers
        for (OrderObserver observer : observers) {
            observer.orderPlaced(order);
        }
    }
}
