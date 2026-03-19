package destinations;

public class DatabaseLogDestination implements LogDestinationImplementor {

    @Override
    public void write(String formattedLog) {
        // Simulacao de persistencia em base de dados.
        System.out.println("[DB] " + formattedLog);
    }
}
