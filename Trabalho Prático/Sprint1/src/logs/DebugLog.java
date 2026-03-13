package logs;

public class DebugLog extends LogEntry {

    public DebugLog(String message) {
        super(message);
    }

    @Override
    public String getLevel() {
        return "DEBUG";
    }
}

