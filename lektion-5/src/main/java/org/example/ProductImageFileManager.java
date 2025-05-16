package org.example;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import javax.imageio.ImageIO;

public class ProductImageFileManager {
    private final Path imageDirectory;

    public ProductImageFileManager(String directoryPath) throws IOException {
        this.imageDirectory = Paths.get(directoryPath);

        if (!Files.exists(imageDirectory)) {
            Files.createDirectories(imageDirectory);
        } else if (!Files.isDirectory(imageDirectory)) {
            throw new IOException(directoryPath + " exists but is not a directory");
        }
    }

    public String saveImage(long productId, byte[] imageData, String fileExtension) throws IOException {
        if (fileExtension == null || !fileExtension.matches("\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("Invalid file extension: " + fileExtension);
        }

        String fileName = "product_" + productId + "_" + System.currentTimeMillis() + fileExtension;
        Path imagePath = imageDirectory.resolve(fileName);

        Files.write(imagePath, imageData);
        return fileName;
    }

    public byte[] readImage(String fileName) throws IOException {
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }

        Path imagePath = imageDirectory.resolve(fileName);
        return Files.readAllBytes(imagePath);
    }

    public void deleteImage(String fileName) throws IOException {
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }

        Path imagePath = imageDirectory.resolve(fileName);
        Files.delete(imagePath);
    }

    public List<String> getProductImages(long productId) throws IOException {
        List<String> imageFiles = new ArrayList<>();
        String prefix = "product_" + productId + "_";

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(imageDirectory,
                             path -> path.getFileName().toString().startsWith(prefix))) {
            for (Path path : stream) {
                imageFiles.add(path.getFileName().toString());
            }
        }

        return imageFiles;
    }

    public int cleanupOldImages(int maxAgeInDays) throws IOException {
        final long cutoffTime = System.currentTimeMillis() - (maxAgeInDays * 24L * 60L * 60L * 1000L);
        int count = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(imageDirectory)) {
            for (Path path : stream) {
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                if (attrs.creationTime().toMillis() < cutoffTime) {
                    Files.delete(path);
                    count++;
                }
            }
        }

        return count;
    }

    // === THUMBNAIL FUNCTIONALITY ===

    public String saveThumbnail(long productId, byte[] imageData, String fileExtension, int width, int height) throws IOException {
        if (!fileExtension.matches("\\.(jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("Thumbnail only supports .jpg, .jpeg or .png");
        }

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        if (originalImage == null) {
            throw new IOException("Unsupported image format or corrupted image data.");
        }

        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        String fileName = "product_" + productId + "_thumb_" + System.currentTimeMillis() + ".jpg";
        Path thumbnailDir = imageDirectory.resolve("thumbnails");

        if (!Files.exists(thumbnailDir)) {
            Files.createDirectories(thumbnailDir);
        }

        Path thumbnailPath = thumbnailDir.resolve(fileName);
        ImageIO.write(resized, "jpg", thumbnailPath.toFile());

        return fileName;
    }

    public byte[] getThumbnail(String fileName) throws IOException {
        Path thumbnailPath = imageDirectory.resolve("thumbnails").resolve(fileName);
        return Files.readAllBytes(thumbnailPath);
    }
}
