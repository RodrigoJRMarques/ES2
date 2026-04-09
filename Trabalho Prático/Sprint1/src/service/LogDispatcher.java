package service;

import config.LogConfig;
import config.LogDestination;
import config.LogLevel;
import decorators.DispatchAction;
import decorators.NoOpDispatchAction;
import destinations.LogDestinationImplementor;
import factory.LogDestinationFactory;
import logs.LogEntry;

import java.util.Collections;
import java.util.Set;

public class LogDispatcher {

    private final LogBridge logBridge;
    private volatile DispatchAction dispatchAction;

    public LogDispatcher() {
        LogDestinationImplementor destination = LogDestinationFactory.createDestination(LogConfig.getInstance().getDestination());
        this.logBridge = new ApplicationLogBridge(destination);
        this.dispatchAction = new NoOpDispatchAction();
    }

    public void dispatch(LogEntry logEntry) {
        LogConfig config = LogConfig.getInstance();

        LogLevel level;
        try {
            level = LogLevel.fromName(logEntry.getLevel());
        } catch (IllegalArgumentException ex) {
            dispatchAction.onFiltered(logEntry);
            return;
        }

        if (!config.isLevelActive(level) || !config.passesFilters(logEntry)) {
            dispatchAction.onFiltered(logEntry);
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
            dispatchAction.onDispatched(logEntry, configuredDestination, formattedLog);
        }
    }

    public void setDispatchAction(DispatchAction dispatchAction) {
        if (dispatchAction == null) {
            this.dispatchAction = new NoOpDispatchAction();
            return;
        }
        this.dispatchAction = dispatchAction;
    }
}
