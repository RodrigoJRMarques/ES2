package logs;

public class ErrorLog extends LogEntry {

    public ErrorLog(String message) {
        super(message);
    }

    @Override
    public String getLevel() {
        return "ERROR";
    }
}
