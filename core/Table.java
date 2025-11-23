package core;

import storage.ToonFileStorage;

import java.util.List;
import java.util.Map;

public class Table {

    private final String name;
    private final ToonFileStorage storage;

    public Table(String name, ToonFileStorage storage) {
        this.name = name;
        this.storage = storage;
    }

    public List<Map<String,Object>> read() {
        return storage.readTable(name);
    }

    public void write(List<Map<String,Object>> rows) {
        storage.writeTable(name, rows);
    }

    public String getName() {
        return name;
    }
}
