package decorators;

import config.LogDestination;
import logs.LogEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ErrorPatternAnalysisDecorator extends DispatchActionDecorator {

    private final Map<String, AtomicInteger> errorPatterns;
    private final int threshold;

    public ErrorPatternAnalysisDecorator(DispatchAction next, int threshold) {
        super(next);
        this.errorPatterns = new ConcurrentHashMap<>();
        this.threshold = threshold;
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        if (!"ERROR".equalsIgnoreCase(logEntry.getLevel())) {
            super.onDispatched(logEntry, destination, formattedLog);
            return;
        }

        int matches = errorPatterns
            .computeIfAbsent(logEntry.getMessage(), ignored -> new AtomicInteger(0))
            .incrementAndGet();

        if (matches >= threshold) {
            System.out.println("[PATTERN ANALYSIS] Padrao de erro repetido detetado (" + matches + "x): " + logEntry.getMessage());
        }

        super.onDispatched(logEntry, destination, formattedLog);
    }
}
