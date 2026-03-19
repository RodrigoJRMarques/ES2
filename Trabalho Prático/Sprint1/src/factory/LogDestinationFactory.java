package factory;

import config.LogDestination;
import destinations.ConsoleLogDestination;
import destinations.DatabaseLogDestination;
import destinations.FileLogDestination;
import destinations.LogDestinationImplementor;
import destinations.RemoteLogDestination;

public class LogDestinationFactory {

    public static LogDestinationImplementor createDestination(LogDestination destination) {
        if (destination == null) {
            return new ConsoleLogDestination();
        }

        switch (destination) {
            case CONSOLE:
                return new ConsoleLogDestination();
            case FILE:
                return new FileLogDestination();
            case DATABASE:
                return new DatabaseLogDestination();
            case REMOTE:
                return new RemoteLogDestination();
            default:
                throw new IllegalArgumentException("Destino de log invalido");
        }
    }
}
