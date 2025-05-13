package com.example.catalog;

public class SubscriptionCartItem implements CartItem {
    private String serviceName;
    private double monthlyFee;

    public SubscriptionCartItem(String serviceName, double monthlyFee) {
        this.serviceName = serviceName;
        this.monthlyFee = monthlyFee;
    }

    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public double getPrice() {
        return monthlyFee;
    }
}
