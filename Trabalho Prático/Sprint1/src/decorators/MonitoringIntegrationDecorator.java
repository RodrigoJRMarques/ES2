package decorators;

import config.LogDestination;
import logs.LogEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitoringIntegrationDecorator extends DispatchActionDecorator {

    private final Map<String, AtomicInteger> counters;

    public MonitoringIntegrationDecorator(DispatchAction next) {
        super(next);
        this.counters = new ConcurrentHashMap<>();
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        int value = counters.computeIfAbsent(logEntry.getLevel(), ignored -> new AtomicInteger(0)).incrementAndGet();
        System.out.println("[MONITORING] level=" + logEntry.getLevel() + " count=" + value + " destino=" + destination);
        super.onDispatched(logEntry, destination, formattedLog);
    }
}
