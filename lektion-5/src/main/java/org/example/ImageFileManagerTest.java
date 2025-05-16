package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class ImageFileManagerTest {
    private static final Logger logger = Logger.getLogger(ImageFileManagerTest.class.getName());

    public static void main(String[] args) {
        try {
            ProductImageFileManager manager = new ProductImageFileManager("product-images");

            long productId = 123;
            byte[] fakeImageData = "fake image content".getBytes();
            String extension = ".png";

            // 1. Spara bild
            String fileName = manager.saveImage(productId, fakeImageData, extension);
            logger.info("Image saved: " + fileName);

            // 2. Läs in bild
            byte[] readData = manager.readImage(fileName);
            logger.info("Image read successfully. Size: " + readData.length + " bytes");

            // 3. Hämta alla bilder för produkt
            List<String> productImages = manager.getProductImages(productId);
            logger.info("Images for product " + productId + ": " + productImages);

            // 4. Ta bort bild
            manager.deleteImage(fileName);
            logger.info("Image deleted: " + fileName);

            // 5. Rensa gamla bilder (0 dagar = alla gamla direkt)
            int deleted = manager.cleanupOldImages(0);
            logger.info("Old images deleted: " + deleted);

        } catch (IOException e) {
            logger.severe("I/O error: " + e.getMessage());
        }
    }
}
