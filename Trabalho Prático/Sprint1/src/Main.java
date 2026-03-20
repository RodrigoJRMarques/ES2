import composite.LogCategory;
import composite.LogLeaf;
import config.LogConfig;
import config.LogDestination;
import config.LogLevel;
import factory.LogFactory;
import logs.LogEntry;
import object_pool.FormatterPool;
import object_pool.LogFormatter;
import service.LogDispatcher;

public class Main {

    public static void main(String[] args) {

        // Modulo 1 - Singleton
        System.out.println("Singleton");
        LogConfig config = LogConfig.getInstance();

        config.setLogLevel(LogLevel.DEBUG);
        config.setDestination(LogDestination.FILE);
        config.setFilePath("logs/app.log");

        System.out.println("Log Level: " + config.getLogLevel());
        System.out.println("Destination: " + config.getDestination());
        System.out.println("File: " + config.getFilePath());

        // Modulo 2 - Factory
        System.out.println("\nFactory");
        LogEntry log1 = LogFactory.createLog("INFO", "Aplicação iniciada");
        LogEntry log2 = LogFactory.createLog("ERROR", "Erro na base de dados");

        // Modulo 3 - Bridge
        System.out.println("\nBridge");
        LogDispatcher dispatcher = new LogDispatcher();
        dispatcher.dispatch(log1);
        dispatcher.dispatch(log2);

        // Troca de destino em runtime sem alterar o resto do sistema.
        config.setDestination(LogDestination.CONSOLE);
        LogEntry log3 = LogFactory.createLog("DEBUG", "Destino alterado em runtime");
        dispatcher.dispatch(log3);

        // Modulo 4 - Composite
        System.out.println("\nComposite");
        LogCategory auth = new LogCategory("Autenticação");
        LogCategory db = new LogCategory("Base de Dados");

        LogEntry log4 = LogFactory.createLog("INFO", "Login efetuado");
        LogEntry log5 = LogFactory.createLog("ERROR", "Erro na query");

        auth.add(new LogLeaf(log1));
        db.add(new LogLeaf(log2));

        LogCategory root = new LogCategory("Sistema");
        root.add(auth);
        root.add(db);

        root.display();

        // Modulo 5 - Object Pool
        System.out.println("\nObject Pool");

        // Reutiliza a instancia
        FormatterPool pool = FormatterPool.getInstance();

        LogFormatter formatter = pool.acquire();

        String formatted = formatter.format(
                LogFactory.createLog("INFO", "Teste")
        );

        System.out.println(formatted);

        pool.release(formatter);


    }
}
