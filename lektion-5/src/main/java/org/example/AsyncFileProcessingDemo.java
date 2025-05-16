package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;



public class AsyncFileProcessingDemo {
    public static void main(String[] args) {
        Path filePath = Paths.get("large_file.txt");

        AsyncFileProcessor processor = new AsyncFileProcessor(4); // 4 trådar

        System.out.println("Starting asynchronous file processing...");

        CompletableFuture<AsyncFileProcessor.ProcessingResult> future =
                processor.processFileAsync(filePath, progress -> {
                    System.out.println("Progress: " + progress + "%");
                });

        // Gör något annat medan filen bearbetas...
        System.out.println("File is being processed in the background.");
        System.out.println("Main thread is free to do other work.");

        // Vänta på resultat och hantera det
        future.thenAccept(result -> {
            System.out.println("Processing completed!");
            System.out.println(result);

            if (result.isSuccessful()) {
                System.out.println("Total lines: " + result.getLineCount());
                System.out.println("Total characters: " + result.getCharacterCount());
            } else {
                System.err.println("Error occurred: " + result.getErrorMessage());
            }
        }).exceptionally(ex -> {
            System.err.println("Unexpected error: " + ex.getMessage());
            return null;
        }).join(); // Vänta på att bearbetningen ska slutföras

        processor.shutdown();
    }

}
