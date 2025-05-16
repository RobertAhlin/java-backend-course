package org.example.handler;

import org.example.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), path);
        response.addDetail("productId", ex.getProductId());

        return response;
    }

    private ErrorResponse handleInsufficientStockException(InsufficientStockException ex, String path) {
        logger.warn("Insufficient stock for product {}: requested={}, available={}",
                ex.getProductId(), ex.getRequestedQuantity(), ex.getAvailableQuantity());

        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), path);
        response.addDetail("productId", ex.getProductId());
        response.addDetail("requestedQuantity", ex.getRequestedQuantity());
        response.addDetail("availableQuantity", ex.getAvailableQuantity());

        return response;
    }

    private ErrorResponse handleGenericException(Exception ex, String path) {
        logger.error("Unexpected error", ex);

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

    private boolean isDevelopmentEnvironment() {
        return true; // Justera enligt miljö
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
