package service;

import config.LogConfig;
import destinations.LogDestinationImplementor;
import factory.LogDestinationFactory;
import logs.LogEntry;

public class LogDispatcher {

    private final LogBridge logBridge;

    public LogDispatcher() {
        LogDestinationImplementor destination = LogDestinationFactory.createDestination(LogConfig.getInstance().getDestination());
        this.logBridge = new ApplicationLogBridge(destination);
    }

    public void dispatch(LogEntry logEntry) {
        LogDestinationImplementor destination = LogDestinationFactory.createDestination(LogConfig.getInstance().getDestination());
        logBridge.setDestination(destination);
        logBridge.send(logEntry);
    }
}
