package ast;

import java.util.Map;

public record UpdateStmt(
        String table,
        Map<String,Object> assignments,
        String whereField,
        Object whereValue
) implements Statement {}
