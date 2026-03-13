package factory;

import logs.*;

public class LogFactory {

    public static LogEntry createLog(String type, String message) {

        switch (type.toUpperCase()) {

            case "INFO":
                return new InfoLog(message);

            case "WARNING":
                return new WarningLog(message);

            case "ERROR":
                return new ErrorLog(message);

            case "DEBUG":
                return new DebugLog(message);

            default:
                throw new IllegalArgumentException("Tipo de log inválido");
        }
    }
}
