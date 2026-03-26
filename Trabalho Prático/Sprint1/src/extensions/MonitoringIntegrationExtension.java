package extensions;

import config.LogDestination;
import logs.LogEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitoringIntegrationExtension implements LogExtension {

    private final Map<String, AtomicInteger> counters;

    public MonitoringIntegrationExtension() {
        this.counters = new ConcurrentHashMap<>();
    }

    @Override
    public String getName() {
        return "MonitoringIntegrationExtension";
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        int value = counters.computeIfAbsent(logEntry.getLevel(), ignored -> new AtomicInteger(0)).incrementAndGet();
        System.out.println("[MONITORING] level=" + logEntry.getLevel() + " count=" + value + " destino=" + destination);
    }
}