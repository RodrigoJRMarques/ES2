import config.LogConfig;
import config.LogDestination;
import config.LogLevel;
import factory.LogFactory;
import logs.LogEntry;
import service.LogDispatcher;

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

        // Modulo 3
        LogDispatcher dispatcher = new LogDispatcher();
        dispatcher.dispatch(log1);
        dispatcher.dispatch(log2);

        // Troca de destino em runtime sem alterar o resto do sistema.
        config.setDestination(LogDestination.CONSOLE);
        LogEntry log3 = LogFactory.createLog("DEBUG", "Destino alterado em runtime");
        dispatcher.dispatch(log3);
    }
}
