package destinations;

public class ConsoleLogDestination implements LogDestinationImplementor {

    @Override
    public void write(String formattedLog) {
        System.out.println(formattedLog);
    }
}
