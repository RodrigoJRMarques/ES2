package service;

import config.LogConfig;
import config.LogDestination;
import config.LogLevel;
import destinations.LogDestinationImplementor;
import extensions.LogExtension;
import factory.LogDestinationFactory;
import logs.LogEntry;

import java.util.Collections;
import java.util.Set;

public class LogDispatcher {

    private final LogBridge logBridge;
    private final LogExtensionManager extensionManager;

    public LogDispatcher() {
        LogDestinationImplementor destination = LogDestinationFactory.createDestination(LogConfig.getInstance().getDestination());
        this.logBridge = new ApplicationLogBridge(destination);
        this.extensionManager = new LogExtensionManager();
    }

    public void dispatch(LogEntry logEntry) {
        LogConfig config = LogConfig.getInstance();

        LogLevel level;
        try {
            level = LogLevel.fromName(logEntry.getLevel());
        } catch (IllegalArgumentException ex) {
            extensionManager.notifyFiltered(logEntry);
            return;
        }

        if (!config.isLevelActive(level) || !config.passesFilters(logEntry)) {
            extensionManager.notifyFiltered(logEntry);
            return;
        }

        Set<LogDestination> destinations = config.getActiveDestinations();
        if (destinations.isEmpty()) {
            destinations = Collections.singleton(config.getDestination());
        }

        String formattedLog = logEntry.format();
        for (LogDestination configuredDestination : destinations) {
            LogDestinationImplementor destination = LogDestinationFactory.createDestination(configuredDestination);
            logBridge.setDestination(destination);
            logBridge.send(logEntry);
            extensionManager.notifyDispatched(logEntry, configuredDestination, formattedLog);
        }
    }

    public void registerExtension(LogExtension extension) {
        extensionManager.register(extension);
    }

    public void unregisterExtension(LogExtension extension) {
        extensionManager.unregister(extension);
    }

    public LogExtensionManager getExtensionManager() {
        return extensionManager;
    }
}
