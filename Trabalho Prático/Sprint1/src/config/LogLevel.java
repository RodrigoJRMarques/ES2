package config;

public enum LogLevel {
    DEBUG,
    INFO,
    WARNING,
    ERROR;

    public static LogLevel fromName(String levelName) {
        return LogLevel.valueOf(levelName.toUpperCase());
    }
}