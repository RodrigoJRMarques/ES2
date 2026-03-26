package service;

import config.LogDestination;
import extensions.LogExtension;
import logs.LogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogExtensionManager {

    private final List<LogExtension> extensions;

    public LogExtensionManager() {
        this.extensions = new CopyOnWriteArrayList<>();
    }

    public void register(LogExtension extension) {
        extensions.add(extension);
    }

    public void unregister(LogExtension extension) {
        extensions.remove(extension);
    }

    public List<LogExtension> getRegisteredExtensions() {
        return Collections.unmodifiableList(new ArrayList<>(extensions));
    }

    public void notifyFiltered(LogEntry entry) {
        for (LogExtension extension : extensions) {
            extension.onFiltered(entry);
        }
    }

    public void notifyDispatched(LogEntry entry, LogDestination destination, String formattedLog) {
        for (LogExtension extension : extensions) {
            extension.onDispatched(entry, destination, formattedLog);
        }
    }
}