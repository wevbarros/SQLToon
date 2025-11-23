package ast;

import java.util.List;

public record SelectStmt(
        List<String> fields,
        String table,
        String whereField,
        Object whereValue
) implements Statement {}
