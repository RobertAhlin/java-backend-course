package org.example;

public class InventorySystem implements OrderObserver {

    @Override
    public void orderPlaced(Order order) {
        System.out.println("[InventorySystem] Uppdaterar lager för beställning: " + order.getOrderId());
    }
}
