package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class AsyncFileCopyDemo {
    public static void main(String[] args) {
        Path source = Paths.get("large_file.txt");
        Path target = Paths.get("copy_of_large_file.txt");

        AsyncFileProcessor processor = new AsyncFileProcessor(4);

        System.out.println("Starting async file copy...");

        CompletableFuture<Void> future = processor.copyFileAsync(source, target, progress -> {
            System.out.println("Copy progress: " + progress + "%");
        });

        future.thenRun(() -> {
            System.out.println("File copy complete!");
        }).exceptionally(ex -> {
            System.err.println("Copy failed: " + ex.getMessage());
            return null;
        }).join();

        processor.shutdown();
    }
}
