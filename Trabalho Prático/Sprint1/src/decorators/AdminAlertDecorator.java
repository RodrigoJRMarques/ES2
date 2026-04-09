package decorators;

import config.LogDestination;
import logs.LogEntry;

public class AdminAlertDecorator extends DispatchActionDecorator {

    public AdminAlertDecorator(DispatchAction next) {
        super(next);
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        if ("ERROR".equalsIgnoreCase(logEntry.getLevel())) {
            System.out.println("[ADMIN ALERT] Erro critico enviado para " + destination + ": " + logEntry.getMessage());
        }
        super.onDispatched(logEntry, destination, formattedLog);
    }
}
