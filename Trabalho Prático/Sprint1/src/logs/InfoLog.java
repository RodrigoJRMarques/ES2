package logs;

public class InfoLog extends LogEntry {

    public InfoLog(String message) {
        super(message);
    }

    @Override
    public String getLevel() {
        return "INFO";
    }
}
