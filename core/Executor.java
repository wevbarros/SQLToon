package core;

import ast.*;

import java.util.*;
import java.util.stream.Collectors;

public class Executor {

    private final Database db;

    public Executor(Database db) {
        this.db = db;
    }

    public Object execute(Statement stmt) {
        if (stmt instanceof InsertStmt s) return insert(s);
        if (stmt instanceof SelectStmt s) return select(s);
        if (stmt instanceof UpdateStmt s) return update(s);
        if (stmt instanceof DeleteStmt s) return delete(s);
        throw new RuntimeException("Erro: comando desconhecido.");
    }

    private Object insert(InsertStmt s) {
        Table t = db.table(s.table());
        List<Map<String,Object>> rows = new ArrayList<>(t.read());

        Map<String,Object> row = new LinkedHashMap<>();
        for (int i = 0; i < s.columns().size(); i++)
            row.put(s.columns().get(i), s.values().get(i));

        rows.add(row);
        t.write(rows);
        return null;
    }

    private Object select(SelectStmt s) {
        Table t = db.table(s.table());
        List<Map<String,Object>> rows = t.read();

        List<Map<String,Object>> result = rows.stream()
            .filter(r -> s.whereField() == null ||
                         Objects.equals(r.get(s.whereField()), s.whereValue()))
            .map(r -> {
                if (s.fields().get(0).equals("*")) return r;
                Map<String,Object> out = new LinkedHashMap<>();
                for (String f : s.fields()) out.put(f, r.get(f));
                return out;
            })
            .collect(Collectors.toList());

        return result;
    }

    private Object update(UpdateStmt s) {
        Table t = db.table(s.table());
        List<Map<String,Object>> rows = new ArrayList<>(t.read());

        for (Map<String,Object> r : rows) {
            if (s.whereField() == null ||
                Objects.equals(r.get(s.whereField()), s.whereValue())) {

                for (var e : s.assignments().entrySet())
                    r.put(e.getKey(), e.getValue());
            }
        }

        t.write(rows);
        return null;
    }

    private Object delete(DeleteStmt s) {
        Table t = db.table(s.table());

        List<Map<String,Object>> remaining = t.read().stream()
            .filter(r -> !Objects.equals(r.get(s.whereField()), s.whereValue()))
            .collect(Collectors.toList());

        t.write(remaining);
        return null;
    }
}
