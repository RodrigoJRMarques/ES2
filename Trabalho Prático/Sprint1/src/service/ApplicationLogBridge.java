package service;

import destinations.LogDestinationImplementor;
import logs.LogEntry;

public class ApplicationLogBridge extends LogBridge {

    public ApplicationLogBridge(LogDestinationImplementor destination) {
        super(destination);
    }

    @Override
    public void send(LogEntry logEntry) {
        super.send(logEntry);
    }
}
