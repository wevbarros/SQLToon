package ast;

public record DeleteStmt(
        String table,
        String whereField,
        Object whereValue
) implements Statement {}
