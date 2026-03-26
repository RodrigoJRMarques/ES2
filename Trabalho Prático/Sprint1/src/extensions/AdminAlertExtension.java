package extensions;

import config.LogDestination;
import logs.LogEntry;

public class AdminAlertExtension implements LogExtension {

    @Override
    public String getName() {
        return "AdminAlertExtension";
    }

    @Override
    public void onDispatched(LogEntry logEntry, LogDestination destination, String formattedLog) {
        if ("ERROR".equalsIgnoreCase(logEntry.getLevel())) {
            System.out.println("[ADMIN ALERT] Erro critico enviado para " + destination + ": " + logEntry.getMessage());
        }
    }
}