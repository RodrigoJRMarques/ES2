package composite;
import logs.LogEntry;

// Log individual (folha)
public class LogLeaf extends LogComponent {

    private LogEntry log;

    public LogLeaf(LogEntry log) {
        this.log = log;
    }

    @Override
    public void display() {
        System.out.println(log.format());
    }
}
