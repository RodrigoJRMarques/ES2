package composite;

import java.util.ArrayList;
import java.util.List;

// Composite
public class LogCategory extends LogComponent {

    private String name;
    private List<LogComponent> children = new ArrayList<>();

    public LogCategory(String name) {
        this.name = name;
    }

    @Override
    public void add(LogComponent component) {
        children.add(component);
    }

    @Override
    public void remove(LogComponent component) {
        children.remove(component);
    }

    @Override
    public void display() {
        System.out.println("Categoria: " + name);
        for (LogComponent c : children) {
            c.display();
        }
    }
}
