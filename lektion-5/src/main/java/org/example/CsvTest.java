package org.example;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class CsvTest {
    private static final Logger logger = Logger.getLogger(CsvTest.class.getName());

    public static void main(String[] args) {
        ProductCsvService csvService = new ProductCsvService();

        List<Product> products = List.of(
                new Product(1, "Laptop", "High-end laptop", 14999.99, 5, "Electronics",
                        "https://example.com/img/laptop.png", "WH01"),
                new Product(2, "Chair", "Ergonomic office chair", 1999.50, 10, "Furniture",
                        "https://example.com/img/chair.png", "WH02")
        );

        Path path = Path.of("products.csv");

        try {
            // Exportera produkter till CSV
            csvService.exportToCsv(products, path);
            logger.info("Export succeeded.");

            // Importera produkter från CSV
            List<Product> imported = csvService.importFromCsv(path);
            logger.info("Import succeeded. Products read: " + imported.size());

            for (Product p : imported) {
                logger.info(p.getName() + " - " + p.getPrice() + " SEK");
            }

        } catch (Exception e) {
            logger.severe("An error occurred during CSV test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
