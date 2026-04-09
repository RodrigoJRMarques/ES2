package decorators;

import config.LogDestination;
import logs.LogEntry;

public interface DispatchAction {

    default void onFiltered(LogEntry logEntry) {
    }

    default void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
    }
}
