package core;

import storage.ToonFileStorage;

public class Database {
    private final ToonFileStorage storage;

    public Database(String dir) {
        this.storage = new ToonFileStorage(dir);
    }

    public Table table(String name) {
        return new Table(name, storage);
    }
}
