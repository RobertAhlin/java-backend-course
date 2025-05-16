package org.example.handler;

import org.example.exception.*;
import org.example.logging.LoggingService;
import org.example.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final LoggingService fileLogger;

    public GlobalExceptionHandler() {
        LoggingService loggerInstance = null;
        try {
            loggerInstance = new LoggingService("logs", "error-log", 5, LogLevel.ERROR);
        } catch (IOException e) {
            System.err.println("Could not initialize LoggingService: " + e.getMessage());
        }
        this.fileLogger = loggerInstance;
    }

    public ErrorResponse handleException(Exception ex, String path) {
        if (ex instanceof ProductNotFoundException) {
            return handleProductNotFoundException((ProductNotFoundException) ex, path);
        } else if (ex instanceof InsufficientStockException) {
            return handleInsufficientStockException((InsufficientStockException) ex, path);
        } else {
            return handleGenericException(ex, path);
        }
    }

    private ErrorResponse handleProductNotFoundException(ProductNotFoundException ex, String path) {
        logger.warn("Product not found: {}", ex.getProductId());
        logToFile("Product not found: " + ex.getProductId());

        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), path);
        response.addDetail("productId", ex.getProductId());

        return response;
    }

    private ErrorResponse handleInsufficientStockException(InsufficientStockException ex, String path) {
        logger.warn("Insufficient stock for product {}: requested={}, available={}",
                ex.getProductId(), ex.getRequestedQuantity(), ex.getAvailableQuantity());

        logToFile("Insufficient stock: productId=" + ex.getProductId()
                + ", requested=" + ex.getRequestedQuantity()
                + ", available=" + ex.getAvailableQuantity());

        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), path);
        response.addDetail("productId", ex.getProductId());
        response.addDetail("requestedQuantity", ex.getRequestedQuantity());
        response.addDetail("availableQuantity", ex.getAvailableQuantity());

        return response;
    }

    private ErrorResponse handleGenericException(Exception ex, String path) {
        logger.error("Unexpected error", ex);
        logToFile("Generic error: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred. Please try again later.",
                path
        );

        if (isDevelopmentEnvironment()) {
            response.addDetail("exception", ex.getClass().getName());
            response.addDetail("stackTrace", getStackTraceAsString(ex));
        }

        return response;
    }

    private void logToFile(String message) {
        if (fileLogger != null) {
            try {
                fileLogger.error(message);
            } catch (IOException e) {
                System.err.println("Failed to write to file logger: " + e.getMessage());
            }
        }
    }

    private boolean isDevelopmentEnvironment() {
        return true; // Kan anpassas senare
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
