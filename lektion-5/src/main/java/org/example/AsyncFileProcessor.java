package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class AsyncFileProcessor {
    private final ExecutorService executorService;

    public AsyncFileProcessor(int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    /**
     * Bearbetar en fil asynkront och returnerar en CompletableFuture.
     *
     * @param filePath Sökvägen till filen som ska bearbetas
     * @param progressCallback Callback för framstegsuppdateringar
     * @return CompletableFuture som blir klar när bearbetningen är klar
     */
    public CompletableFuture<ProcessingResult> processFileAsync(
            Path filePath,
            Consumer<Integer> progressCallback) {

        return CompletableFuture.supplyAsync(() -> {
            ProcessingResult result = new ProcessingResult();

            try {
                // Kontrollera att filen finns
                if (!Files.exists(filePath)) {
                    throw new FileNotFoundException("File not found: " + filePath);
                }

                // Hämta filstorlek för framstegsberäkning
                long fileSize = Files.size(filePath);

                try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                    String line;
                    long bytesRead = 0;
                    int lineCount = 0;
                    int lastProgress = 0;

                    while ((line = reader.readLine()) != null) {
                        // Bearbeta raden (här bara räknar vi rader och tecken)
                        result.incrementLines();
                        result.addCharacters(line.length());

                        // Uppdatera bytesRead approximativt
                        bytesRead += line.length() + 1; // +1 för radbrytning
                        lineCount++;

                        // Uppdatera framsteg periodiskt (inte för varje rad för prestanda)
                        if (lineCount % 100 == 0 || bytesRead >= fileSize) {
                            int progress = (int)((bytesRead * 100) / fileSize);
                            if (progress > lastProgress) {
                                progressCallback.accept(progress);
                                lastProgress = progress;
                            }
                        }

                        // Simulera lite arbete för att göra asynkronitet mer märkbar
                        if (lineCount % 1000 == 0) {
                            Thread.sleep(1);
                        }
                    }

                    // Slutlig framstegsuppdatering
                    progressCallback.accept(100);

                    result.setSuccessful(true);
                }
            } catch (Exception e) {
                result.setSuccessful(false);
                result.setErrorMessage(e.getMessage());
                result.setException(e);
            }

            return result;
        }, executorService);
    }

    /**
     * Stänger executor service när den inte längre behövs.
     */
    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * Försöker stänga executor service och avbryter pågående uppgifter om nödvändigt.
     */
    public void shutdownNow() {
        executorService.shutdownNow();
    }

    /**
     * Resultatobjekt för filbearbetning.
     */
    public static class ProcessingResult {
        private boolean successful;
        private int lineCount;
        private int characterCount;
        private String errorMessage;
        private Exception exception;

        public ProcessingResult() {
            this.successful = false;
            this.lineCount = 0;
            this.characterCount = 0;
        }

        public void incrementLines() {
            this.lineCount++;
        }

        public void addCharacters(int count) {
            this.characterCount += count;
        }

        // Getters and setters
        public boolean isSuccessful() { return successful; }
        public void setSuccessful(boolean successful) { this.successful = successful; }

        public int getLineCount() { return lineCount; }
        public void setLineCount(int lineCount) { this.lineCount = lineCount; }

        public int getCharacterCount() { return characterCount; }
        public void setCharacterCount(int characterCount) { this.characterCount = characterCount; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

        public Exception getException() { return exception; }
        public void setException(Exception exception) { this.exception = exception; }

        @Override
        public String toString() {
            if (successful) {
                return "Successful processing: " + lineCount + " lines, "
                        + characterCount + " characters";
            } else {
                return "Processing failed: " + errorMessage;
            }
        }
    }

    public CompletableFuture<Void> copyFileAsync(
            Path source, Path target, Consumer<Integer> progressCallback) {

        return CompletableFuture.runAsync(() -> {
            try {
                if (!Files.exists(source)) {
                    throw new FileNotFoundException("Source file not found: " + source);
                }

                long totalBytes = Files.size(source);
                long copiedBytes = 0;

                try (InputStream in = Files.newInputStream(source);
                     OutputStream out = Files.newOutputStream(target)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    int lastProgress = 0;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        copiedBytes += bytesRead;

                        int progress = (int)((copiedBytes * 100) / totalBytes);
                        if (progress > lastProgress) {
                            progressCallback.accept(progress);
                            lastProgress = progress;
                        }
                    }

                    progressCallback.accept(100);
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executorService);
    }



    public CompletableFuture<List<ProcessingResult>> processFilesInBatch(
            List<Path> filePaths,
            int maxConcurrent,
            Consumer<String> statusCallback) {

        Semaphore semaphore = new Semaphore(maxConcurrent);
        List<CompletableFuture<ProcessingResult>> futures = new ArrayList<>();

        for (Path filePath : filePaths) {
            CompletableFuture<ProcessingResult> future = CompletableFuture.supplyAsync(() -> {
                try {
                    semaphore.acquire();
                    statusCallback.accept("Started: " + filePath.getFileName());
                    ProcessingResult result = processFileAsync(filePath, progress -> {}).join();
                    statusCallback.accept("Finished: " + filePath.getFileName());
                    return result;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ProcessingResult errorResult = new ProcessingResult();
                    errorResult.setSuccessful(false);
                    errorResult.setErrorMessage("Interrupted");
                    return errorResult;
                } finally {
                    semaphore.release();
                }
            }, executorService);

            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }


}
