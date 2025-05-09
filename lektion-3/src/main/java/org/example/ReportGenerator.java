package org.example;

public abstract class ReportGenerator {

    // Final Template Method – bestämmer ordningen
    public final void generateReport() {
        createHeader();
        fetchData();
        formatContent();
        createFooter();
    }

    // Gemensam header
    protected void createHeader() {
        System.out.println("=== Rapport start ===");
    }

    // Gemensam footer
    protected void createFooter() {
        System.out.println("=== Rapport slut ===");
    }

    // Abstrakta steg – måste implementeras av subklass
    protected abstract void fetchData();
    protected abstract void formatContent();
}
