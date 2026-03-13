import config.LogConfig;
import config.LogDestination;
import config.LogLevel;
import factory.LogFactory;
import logs.LogEntry;

public class Main {

    public static void main(String[] args) {

        // Modulo 1
        LogConfig config = LogConfig.getInstance();

        config.setLogLevel(LogLevel.DEBUG);
        config.setDestination(LogDestination.FILE);
        config.setFilePath("logs/app.log");

        System.out.println("Log Level: " + config.getLogLevel());
        System.out.println("Destination: " + config.getDestination());
        System.out.println("File: " + config.getFilePath());

        // Modulo 2
        LogEntry log1 = LogFactory.createLog("INFO", "Aplicação iniciada");
        LogEntry log2 = LogFactory.createLog("ERROR", "Erro na base de dados");

        System.out.println(log1.format());
        System.out.println(log2.format());
    }
}
