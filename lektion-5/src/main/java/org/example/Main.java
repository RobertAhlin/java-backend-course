package org.example;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ValidationService service = new ValidationService();

        try {
            service.validateEmail("invalid-email");
        } catch (ValidationException e) {
            System.out.println("Email validation failed: " + e.getMessage());
        }

        try {
            service.validatePassword("123");
        } catch (ValidationException e) {
            System.out.println("Password validation failed: " + e.getMessage());
        }

        try {
            Product p = new Product(1, "TV", "Smart-TV", 0.0, 5, "Electronics", "", "WH01");
            service.validateProduct(p);
        } catch (ValidationException e) {
            System.out.println("Product validation failed: " + e.getMessage());
        }

        try {
            Address address = new Address("Storgatan 1", "Stockholm", "12345");
            service.validateAddress(address);
            System.out.println("Valid address passed.");
        } catch (ValidationException e) {
            System.out.println("Address validation failed: " + e.getMessage());
        }

        // === Thumbnail test ===
        try {
            long productId = 123;
            byte[] fakeImageData = "fake png image content".getBytes(); // dummy data
            String extension = ".png";

            ProductImageFileManager manager = new ProductImageFileManager("product-images");

            String thumbnailName = manager.saveThumbnail(productId, fakeImageData, extension, 100, 100);
            logger.info("Thumbnail saved: " + thumbnailName);

            byte[] thumbnailData = manager.getThumbnail(thumbnailName);
            logger.info("Thumbnail size: " + thumbnailData.length + " bytes");

        } catch (Exception e) {
            logger.severe("Thumbnail test failed: " + e.getMessage());
        }

        try {
            ImageDirectoryWatcher watcher = new ImageDirectoryWatcher("product-images");
            watcher.startWatching();
            logger.info("Watcher started. Try adding/removing files manually to see log output.");

            // Testkörningen hålls igång några sekunder
            Thread.sleep(10000);

            watcher.stopWatching();
        } catch (Exception e) {
            logger.severe("Watcher failed: " + e.getMessage());
        }

    }
}
