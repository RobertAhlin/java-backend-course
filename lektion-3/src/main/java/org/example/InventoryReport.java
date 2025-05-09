package org.example;

public class InventoryReport extends ReportGenerator {

    private String inventoryData;

    @Override
    protected void fetchData() {
        // Simulerad lagerinformation
        inventoryData = "Lagerstatus:\n- Bananer: 120 st\n- Äpplen: 200 st\n- Mjölk: 80 st";
        System.out.println("[InventoryReport] Lagerdata hämtad.");
    }

    @Override
    protected void formatContent() {
        System.out.println("** Lagerrapport **");
        System.out.println(inventoryData);
    }
}
