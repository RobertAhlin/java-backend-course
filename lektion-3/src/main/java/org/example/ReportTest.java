package org.example;

public class ReportTest {
    public static void main(String[] args) {
        ReportGenerator sales = new SalesReport();
        sales.generateReport();

        ReportGenerator inventory = new InventoryReport();
        inventory.generateReport();

        ReportGenerator customer = new CustomerReport();
        customer.generateReport();
    }
}