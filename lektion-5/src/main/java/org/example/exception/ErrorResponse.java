package org.example.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final String path;
    private final Map<String, Object> details;

    public ErrorResponse(String errorCode, String message, String path) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.details = new HashMap<>();
    }

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }

    // Getters
    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
