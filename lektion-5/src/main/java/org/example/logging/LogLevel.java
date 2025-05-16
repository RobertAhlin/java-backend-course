package org.example.logging;

public enum LogLevel {
    DEBUG, INFO, WARN, ERROR;

    public boolean isAtLeast(LogLevel other) {
        return this.ordinal() >= other.ordinal();
    }
}
