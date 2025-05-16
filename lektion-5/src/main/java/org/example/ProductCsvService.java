package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;



public class ProductCsvService {
    private static final String HEADER = "id,name,description,price,stockQuantity,category,imageUrl,warehouseId";


    /**
     * Exporterar en lista med produkter till en CSV-fil.
     *
     * @param products Listan med produkter som ska exporteras
     * @param filePath Sökvägen där CSV-filen ska sparas
     * @throws IOException om det uppstår ett fel vid skrivning till filen
     */
    public void exportToCsv(List<Product> products, Path filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Skriv header
            writer.write(HEADER);
            writer.newLine();

            // Skriv produkter
            for (Product product : products) {
                writer.write(String.format("%d,%s,%s,%.2f,%d,%s,%s,%s",
                        product.getId(),
                        escapeField(product.getName()),
                        escapeField(product.getDescription()),
                        product.getPrice(),
                        product.getStockQuantity(),
                        escapeField(product.getCategory()),
                        escapeField(product.getImageUrl()),
                        escapeField(product.getWarehouseId())
                ));
                writer.newLine();
            }
        }
    }

    /**
     * Importerar produkter från en CSV-fil.
     *
     * @param filePath Sökvägen till CSV-filen som ska importeras
     * @return En lista med importerade produkter
     * @throws IOException om det uppstår ett fel vid läsning från filen
     * @throws CsvParseException om CSV-formatet är ogiltigt
     */
    public List<Product> importFromCsv(Path filePath) throws IOException, CsvParseException {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            // Läs och validera header
            String headerLine = reader.readLine();
            if (headerLine == null || !headerLine.equals(HEADER)) {
                throw new CsvParseException("Invalid CSV header. Expected: " + HEADER);
            }

            // Läs produktrader
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Product product = parseProductLine(line);
                    products.add(product);
                } catch (Exception e) {
                    throw new CsvParseException(
                            "Error parsing line " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        }

        return products;
    }

    private Product parseProductLine(String line) throws CsvParseException {
        String[] fields = line.split(",", -1); // -1 behåller tomma fält

        if (fields.length != 8) {
            throw new CsvParseException("Invalid number of fields. Expected 8, got " + fields.length);
        }

        try {
            long id = Long.parseLong(fields[0]);
            String name = fields[1];
            String description = fields[2];
            double price = Double.parseDouble(fields[3]);
            int stockQuantity = Integer.parseInt(fields[4]);
            String category = fields[5];
            String imageUrl = fields[6];
            String warehouseId = fields[7];

            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            product.setImageUrl(imageUrl);
            product.setWarehouseId(warehouseId);

            return product;
        } catch (NumberFormatException e) {
            throw new CsvParseException("Invalid number format: " + e.getMessage(), e);
        }
    }

    private String escapeField(String field) {
        if (field == null) {
            return "";
        }
        // Ersätt kommatecken med något annat för att undvika CSV-parsing problem
        return field.replace(",", ";");
    }
}