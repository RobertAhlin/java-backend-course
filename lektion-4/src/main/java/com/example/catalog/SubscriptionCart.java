package com.example.catalog;

public class SubscriptionCart extends Cart<SubscriptionCartItem> {
    // Exempel på specialmetod för prenumerationer
    public double getTotalAnnualCost() {
        return getTotalPrice() * 12;
    }
}
