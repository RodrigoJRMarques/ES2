package config;

public class LogConfig {

    private static volatile LogConfig instance;

    private LogLevel logLevel;
    private LogDestination destination;
    private String logFormat;
    private String filePath;

    // Construtor
    private LogConfig() {
        // valores por defeito
        logLevel = LogLevel.INFO;
        destination = LogDestination.CONSOLE;
        logFormat = "[%LEVEL%] %MESSAGE%";
        filePath = "app.log";
    }

    // Singleton
    public static LogConfig getInstance() {
        if (instance == null) {
            synchronized (LogConfig.class) {
                if (instance == null) {
                    instance = new LogConfig();
                }
            }
        }
        return instance;
    }

    // Getters e Setters
    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public LogDestination getDestination() {
        return destination;
    }

    public void setDestination(LogDestination destination) {
        this.destination = destination;
    }

    public String getLogFormat() {
        return logFormat;
    }

    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
