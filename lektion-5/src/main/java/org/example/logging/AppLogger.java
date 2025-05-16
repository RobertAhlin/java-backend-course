package org.example.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AppLogger {
    private final Logger logger;

    public AppLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    public void infoWithContext(String message, Map<String, Object> context, Object... args) {
        if (logger.isInfoEnabled()) {
            String contextString = formatContext(context);
            logger.info(contextString + message, args);
        }
    }

    private String formatContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Map.Entry<String, Object> entry : context.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }

        sb.append("] ");
        return sb.toString();
    }
}
