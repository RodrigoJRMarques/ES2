package logs;

public class WarningLog extends LogEntry {

    public WarningLog(String message) {
        super(message);
    }

    @Override
    public String getLevel() {
        return "WARNING";
    }
}
