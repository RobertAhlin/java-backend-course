package org.example;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private Timestamp orderDate;
    private double totalAmount;
    private Status status;
    private int shippingAddressId;
    private int billingAddressId;

    public enum Status {
        PENDING,
        PAID,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

    // Konstruktorer
    public Order() {}

    public Order(int userId, double totalAmount, Status status, int shippingAddressId, int billingAddressId) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddressId = shippingAddressId;
        this.billingAddressId = billingAddressId;
    }

    // Getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(int shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public int getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(int billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", shippingAddressId=" + shippingAddressId +
                ", billingAddressId=" + billingAddressId +
                '}';
    }
}
