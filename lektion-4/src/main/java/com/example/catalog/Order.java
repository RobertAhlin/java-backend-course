package com.example.catalog;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private Long orderId;
    private String customerId;
    private LocalDate orderDate;
    private List<OrderItem> items;

    public Order(Long orderId, String customerId, LocalDate orderDate, List<OrderItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
