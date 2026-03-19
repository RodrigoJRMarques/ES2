package destinations;

public class RemoteLogDestination implements LogDestinationImplementor {

    @Override
    public void write(String formattedLog) {
        // Simulacao de envio para um servico remoto.
        System.out.println("[REMOTE] " + formattedLog);
    }
}
