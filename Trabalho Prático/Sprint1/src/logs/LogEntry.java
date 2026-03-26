package logs;

import java.time.LocalDateTime;

public abstract class LogEntry {

    protected String message;
    protected LocalDateTime timestamp;

    protected LogEntry(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public abstract String getLevel();

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String format() {
        return "[" + timestamp + "] [" + getLevel() + "] " + message;
    }
}

