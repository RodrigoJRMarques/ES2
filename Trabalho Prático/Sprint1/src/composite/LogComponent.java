package composite;

// Componente base
public abstract class LogComponent {

    public void add(LogComponent component) {
        throw new UnsupportedOperationException();
    }

    public void remove(LogComponent component) {
        throw new UnsupportedOperationException();
    }

    public abstract void display();
}
