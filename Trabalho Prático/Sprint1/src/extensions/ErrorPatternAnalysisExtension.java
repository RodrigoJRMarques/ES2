package extensions;

import config.LogDestination;
import logs.LogEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ErrorPatternAnalysisExtension implements LogExtension {

    private final Map<String, AtomicInteger> errorPatterns;
    private final int threshold;

    public ErrorPatternAnalysisExtension(int threshold) {
        this.errorPatterns = new ConcurrentHashMap<>();
        this.threshold = threshold;
    }

    @Override
    public String getName() {
        return "ErrorPatternAnalysisExtension";
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        if (!"ERROR".equalsIgnoreCase(logEntry.getLevel())) {
            return;
        }

        int matches = errorPatterns
            .computeIfAbsent(logEntry.getMessage(), ignored -> new AtomicInteger(0))
            .incrementAndGet();

        if (matches >= threshold) {
            System.out.println("[PATTERN ANALYSIS] Padrao de erro repetido detetado (" + matches + "x): " + logEntry.getMessage());
        }
    }
}