package org.example.logging;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LoggingService implements Closeable {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final Path logDirectory;
    private final String logFilePrefix;
    private final int maxLogFiles;
    private final LogLevel minimumLevel;

    private BufferedWriter currentWriter;
    private LocalDate currentLogDate;

    public LoggingService(String logDir, String prefix, int maxFiles, LogLevel minimumLevel) throws IOException {
        this.logDirectory = Paths.get(logDir);
        this.logFilePrefix = prefix;
        this.maxLogFiles = maxFiles;
        this.minimumLevel = minimumLevel;

        if (!Files.exists(logDirectory)) {
            Files.createDirectories(logDirectory);
        }

        this.currentLogDate = LocalDate.now();
        this.currentWriter = createWriter(currentLogDate);

        cleanupOldLogs();
        compressOldLogs();
    }

    public synchronized void log(String level, String message) throws IOException {
        LogLevel msgLevel = LogLevel.valueOf(level.toUpperCase());
        if (!msgLevel.isAtLeast(minimumLevel)) return;

        checkRotation();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logEntry = String.format("[%s] [%s] %s%n", timestamp, level, message);
        currentWriter.write(logEntry);
        currentWriter.flush();
    }

    public void info(String message) throws IOException {
        log("INFO", message);
    }

    public void warn(String message) throws IOException {
        log("WARN", message);
    }

    public void error(String message) throws IOException {
        log("ERROR", message);
    }

    public void debug(String message) throws IOException {
        log("DEBUG", message);
    }

    @Override
    public synchronized void close() throws IOException {
        if (currentWriter != null) {
            currentWriter.close();
            currentWriter = null;
        }
    }

    private synchronized void checkRotation() throws IOException {
        LocalDate today = LocalDate.now();
        if (!today.equals(currentLogDate)) {
            currentWriter.close();
            currentLogDate = today;
            currentWriter = createWriter(currentLogDate);
            cleanupOldLogs();
            compressOldLogs();
        }
    }

    private BufferedWriter createWriter(LocalDate date) throws IOException {
        String fileName = logFilePrefix + "-" + date.format(DATE_FORMAT) + ".log";
        Path logFile = logDirectory.resolve(fileName);
        return Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private void cleanupOldLogs() throws IOException {
        List<Path> logFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(logDirectory, logFilePrefix + "-*.log")) {
            for (Path path : stream) {
                logFiles.add(path);
            }
        }

        if (logFiles.size() > maxLogFiles) {
            logFiles.sort(Comparator.comparing(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    return attrs.creationTime().toInstant();
                } catch (IOException e) {
                    return Instant.MAX;
                }
            }));

            int filesToDelete = logFiles.size() - maxLogFiles;
            for (int i = 0; i < filesToDelete; i++) {
                Files.delete(logFiles.get(i));
            }
        }
    }

    private void compressOldLogs() {
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(logDirectory, logFilePrefix + "-*.log")) {

            LocalDate today = LocalDate.now();

            for (Path path : stream) {
                String filename = path.getFileName().toString();

                if (filename.endsWith(".log")) {
                    String datePart = filename
                            .substring(logFilePrefix.length() + 1, filename.length() - 4); // yyyy-MM-dd

                    LocalDate fileDate = LocalDate.parse(datePart, DATE_FORMAT);

                    if (fileDate.isBefore(today)) {
                        Path zipPath = logDirectory.resolve(filename.replace(".log", ".zip"));
                        compressToZip(path, zipPath);
                        Files.delete(path);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Log compression failed: " + e.getMessage());
        }
    }

    private void compressToZip(Path inputFile, Path zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile));
             InputStream is = Files.newInputStream(inputFile)) {

            zos.putNextEntry(new ZipEntry(inputFile.getFileName().toString()));
            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }
}
