package org.example;

public class SalesReport extends ReportGenerator {

    private String salesData;

    @Override
    protected void fetchData() {
        // Simulerad datainhämtning
        salesData = "Sålda enheter: 150\nTotalt: 75 000 kr";
        System.out.println("[SalesReport] Data hämtad.");
    }

    @Override
    protected void formatContent() {
        System.out.println("** Försäljningsrapport **");
        System.out.println(salesData);
    }
}
