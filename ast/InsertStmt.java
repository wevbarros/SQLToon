package ast;

import java.util.List;

public record InsertStmt(
        String table,
        List<String> columns,
        List<Object> values
) implements Statement {}
