package decorators;

import config.LogDestination;
import logs.LogEntry;

public abstract class DispatchActionDecorator implements DispatchAction {

    protected final DispatchAction next;

    protected DispatchActionDecorator(DispatchAction next) {
        this.next = next == null ? new NoOpDispatchAction() : next;
    }

    @Override
    public void onFiltered(LogEntry logEntry) {
        next.onFiltered(logEntry);
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        next.onDispatched(logEntry, destination, formattedLog);
    }
}
