package org.example.exception;

import java.time.LocalDateTime;

public abstract class ECommerceException extends Exception {
    private final LocalDateTime timestamp;
    private final String errorCode;

    public ECommerceException(String message, String errorCode) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
    }

    public ECommerceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
