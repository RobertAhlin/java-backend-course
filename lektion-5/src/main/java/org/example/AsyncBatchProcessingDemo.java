package org.example;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Paths;

public class AsyncBatchProcessingDemo {
    public static void main(String[] args) {
        AsyncFileProcessor processor = new AsyncFileProcessor(4);

        List<Path> filesToProcess = List.of(
                Paths.get("large_file_1.txt"),
                Paths.get("large_file_2.txt"),
                Paths.get("large_file_3.txt")
        );

        System.out.println("Starting batch processing...");

        CompletableFuture<List<AsyncFileProcessor.ProcessingResult>> batchFuture =
                processor.processFilesInBatch(filesToProcess, 2, status -> {
                    System.out.println("[Status] " + status);
                });

        batchFuture.thenAccept(results -> {
            System.out.println("Batch processing done.");
            for (int i = 0; i < results.size(); i++) {
                System.out.println("Result for file " + (i + 1) + ": " + results.get(i));
            }
        }).exceptionally(ex -> {
            System.err.println("Batch failed: " + ex.getMessage());
            return null;
        }).join();

        processor.shutdown();
    }
}
