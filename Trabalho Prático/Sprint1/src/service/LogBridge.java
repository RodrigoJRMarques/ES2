package service;

import destinations.LogDestinationImplementor;
import logs.LogEntry;

public class LogBridge {

    protected LogDestinationImplementor destination;

    public LogBridge(LogDestinationImplementor destination) {
        this.destination = destination;
    }

    public void setDestination(LogDestinationImplementor destination) {
        this.destination = destination;
    }

    public void send(LogEntry logEntry) {
        destination.write(logEntry.format());
    }
}
