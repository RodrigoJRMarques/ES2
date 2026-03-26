package filters;

import logs.LogEntry;

public interface LogFilter {

    String getName();

    boolean accept(LogEntry logEntry);
}