package org.example.demo;

import org.example.logging.LoggingService;
import org.example.logging.LogLevel;

import java.io.IOException;

public class LoggingServiceDemo {
    public static void main(String[] args) {
        try (LoggingService logger = new LoggingService("logs", "application", 7, LogLevel.INFO)) {
            logger.info("Application started");

            try {
                int result = 10 / 0; // Simulerat fel
            } catch (Exception e) {
                logger.error("Error occurred: " + e.getMessage());
            }

            logger.debug("This debug message should NOT appear if minimum level is INFO");
            logger.warn("This is a warning");
            logger.info("Application completed");

        } catch (IOException e) {
            System.err.println("Failed to initialize logging: " + e.getMessage());
        }
    }
}
