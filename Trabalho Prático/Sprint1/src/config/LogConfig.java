package config;

import filters.LogFilter;
import logs.LogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LogConfig {

    private static volatile LogConfig instance;

    private LogLevel logLevel;
    private LogDestination destination;
    private String logFormat;
    private String filePath;
    private EnumSet<LogLevel> activeLevels;
    private LinkedHashSet<LogDestination> activeDestinations;
    private List<LogFilter> filters;

    // Construtor
    private LogConfig() {
        // valores por defeito
        logLevel = LogLevel.INFO;
        destination = LogDestination.CONSOLE;
        logFormat = "[%LEVEL%] %MESSAGE%";
        filePath = "app.log";
        activeLevels = EnumSet.allOf(LogLevel.class);
        activeDestinations = new LinkedHashSet<>();
        activeDestinations.add(destination);
        filters = new ArrayList<>();
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
    public synchronized LogLevel getLogLevel() {
        return logLevel;
    }

    public synchronized void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public synchronized LogDestination getDestination() {
        return destination;
    }

    public synchronized void setDestination(LogDestination destination) {
        this.destination = destination;
        this.activeDestinations.clear();
        this.activeDestinations.add(destination);
    }

    public synchronized String getLogFormat() {
        return logFormat;
    }

    public synchronized void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    public synchronized String getFilePath() {
        return filePath;
    }

    public synchronized void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public synchronized void activateLevel(LogLevel level) {
        activeLevels.add(level);
    }

    public synchronized void deactivateLevel(LogLevel level) {
        activeLevels.remove(level);
    }

    public synchronized Set<LogLevel> getActiveLevels() {
        return Collections.unmodifiableSet(EnumSet.copyOf(activeLevels));
    }

    public synchronized boolean isLevelActive(LogLevel level) {
        return activeLevels.contains(level);
    }

    public synchronized boolean isLevelActive(String levelName) {
        try {
            return isLevelActive(LogLevel.fromName(levelName));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public synchronized void addDestination(LogDestination destination) {
        activeDestinations.add(destination);
    }

    public synchronized void removeDestination(LogDestination destination) {
        activeDestinations.remove(destination);
    }

    public synchronized Set<LogDestination> getActiveDestinations() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(activeDestinations));
    }

    public synchronized void addFilter(LogFilter filter) {
        filters.add(filter);
    }

    public synchronized void removeFilter(LogFilter filter) {
        filters.remove(filter);
    }

    public synchronized void clearFilters() {
        filters.clear();
    }

    public synchronized List<LogFilter> getFilters() {
        return Collections.unmodifiableList(new ArrayList<>(filters));
    }

    public synchronized boolean passesFilters(LogEntry logEntry) {
        for (LogFilter filter : filters) {
            if (!filter.accept(logEntry)) {
                return false;
            }
        }
        return true;
    }

    public synchronized LogConfigSnapshot createSnapshot() {
        return new LogConfigMemento(
            logLevel,
            destination,
            logFormat,
            filePath,
            EnumSet.copyOf(activeLevels),
            new LinkedHashSet<>(activeDestinations),
            new ArrayList<>(filters)
        );
    }

    public synchronized void restore(LogConfigSnapshot snapshot) {
        if (!(snapshot instanceof LogConfigMemento)) {
            throw new IllegalArgumentException("Snapshot invalido para LogConfig");
        }

        LogConfigMemento memento = (LogConfigMemento) snapshot;
        this.logLevel = memento.logLevel;
        this.destination = memento.destination;
        this.logFormat = memento.logFormat;
        this.filePath = memento.filePath;
        this.activeLevels = EnumSet.copyOf(memento.activeLevels);
        this.activeDestinations = new LinkedHashSet<>(memento.activeDestinations);
        this.filters = new ArrayList<>(memento.filters);
    }

    private static final class LogConfigMemento implements LogConfigSnapshot {

        private final LogLevel logLevel;
        private final LogDestination destination;
        private final String logFormat;
        private final String filePath;
        private final EnumSet<LogLevel> activeLevels;
        private final LinkedHashSet<LogDestination> activeDestinations;
        private final List<LogFilter> filters;

        private LogConfigMemento(
            LogLevel logLevel,
            LogDestination destination,
            String logFormat,
            String filePath,
            EnumSet<LogLevel> activeLevels,
            LinkedHashSet<LogDestination> activeDestinations,
            List<LogFilter> filters
        ) {
            this.logLevel = logLevel;
            this.destination = destination;
            this.logFormat = logFormat;
            this.filePath = filePath;
            this.activeLevels = activeLevels;
            this.activeDestinations = activeDestinations;
            this.filters = filters;
        }
    }
}
