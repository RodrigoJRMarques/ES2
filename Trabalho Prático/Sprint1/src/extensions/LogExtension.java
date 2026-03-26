package extensions;

import config.LogDestination;
import logs.LogEntry;

public interface LogExtension {

    String getName();

    default void onFiltered(LogEntry logEntry) {
    }

    default void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
    }
}