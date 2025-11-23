package storage;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ToonFileStorage {

    private final Path baseDir;

    public ToonFileStorage(String baseDir) {
        this.baseDir = Paths.get(baseDir);
        try {
            Files.createDirectories(this.baseDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<Map<String,Object>> readTable(String table) {
        Path file = baseDir.resolve(table + ".toon");
        if (!Files.exists(file)) return new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(file);
            if (lines.isEmpty()) return new ArrayList<>();

            String header = lines.get(0);
            if (!header.startsWith("COLUMNS:"))
                throw new RuntimeException("Arquivo TOON inválido.");

            String[] cols = header.substring("COLUMNS:".length())
                    .trim().split(",");

            List<Map<String,Object>> rows = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\t", -1);
                Map<String,Object> row = new LinkedHashMap<>();

                for (int c = 0; c < cols.length; c++) {
                    String raw = unescape(parts[c]);
                    row.put(cols[c], tryNum(raw));
                }

                rows.add(row);
            }

            return rows;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void writeTable(String table, List<Map<String,Object>> rows) {
        Path file = baseDir.resolve(table + ".toon");

        try {
            List<String> out = new ArrayList<>();

            LinkedHashSet<String> cols = new LinkedHashSet<>();
            for (Map<String,Object> r : rows)
                cols.addAll(r.keySet());

            out.add("COLUMNS: " + String.join(",", cols));

            for (Map<String,Object> r : rows) {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                for (String col : cols) {
                    if (i++ > 0) sb.append('\t');
                    Object val = r.get(col);
                    sb.append(escape(val == null ? "" : val.toString()));
                }
                out.add(sb.toString());
            }

            Files.write(file, out,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Helpers
    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

    private static String unescape(String s) {
        return s.replace("\\t", "\t")
                .replace("\\n", "\n")
                .replace("\\\\", "\\");
    }

    private static Object tryNum(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return s; }
    }
}
