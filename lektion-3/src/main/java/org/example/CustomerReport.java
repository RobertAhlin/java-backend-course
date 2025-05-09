package org.example;

public class CustomerReport extends ReportGenerator {

    private String customerData;

    @Override
    protected void fetchData() {
        customerData = "Kunder:\n- Anna Karlsson\n- Bo Svensson\n- Cecilia Lind";
        System.out.println("[CustomerReport] Kunddata hämtad.");
    }

    @Override
    protected void formatContent() {
        System.out.println("** Kundrapport **");
        System.out.println(customerData);
    }
}
