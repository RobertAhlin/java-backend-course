package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ImageDirectoryWatcher {
    private static final Logger logger = Logger.getLogger(ImageDirectoryWatcher.class.getName());
    private final Path directoryToWatch;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ImageDirectoryWatcher(String directoryPath) {
        this.directoryToWatch = Paths.get(directoryPath);
    }

    public void startWatching() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        // Registrera katalogen för förändringar
        directoryToWatch.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
        );

        logger.info("Watching directory: " + directoryToWatch.toAbsolutePath());

        // Kör övervakningen i en separat tråd
        executor.submit(() -> {
            while (true) {
                try {
                    WatchKey key = watchService.take(); // väntar på händelse

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path changed = (Path) event.context();
                        logger.info("Event: " + kind.name() + " - File: " + changed);
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warning("Watcher interrupted.");
                    break;
                }
            }
        });
    }

    public void stopWatching() {
        executor.shutdownNow();
        logger.info("Stopped watching directory.");
    }
}
