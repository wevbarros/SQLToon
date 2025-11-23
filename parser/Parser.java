package parser;

import lexer.*;
import ast.*;

import java.util.*;

public class Parser {

    private final Lexer lexer;
    private Token current;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.current = lexer.next();
    }

    private void eat(TokenType type) {
        if (current.type != type)
            throw new RuntimeException("Esperado: " + type + " encontrado: " + current);
        current = lexer.next();
    }

    public Statement parseStatement() {
        return switch (current.type) {
            case INSERT -> parseInsert();
            case SELECT -> parseSelect();
            case UPDATE -> parseUpdate();
            case DELETE -> parseDelete();
            case EOF -> null;
            default -> throw new RuntimeException("Comando inesperado: " + current);
        };
    }

    private InsertStmt parseInsert() {
        eat(TokenType.INSERT);
        eat(TokenType.INTO);

        String table = current.text.toLowerCase();
        eat(TokenType.IDENTIFIER);

        eat(TokenType.LPAREN);
        List<String> cols = parseIdentList();
        eat(TokenType.RPAREN);

        eat(TokenType.VALUES);

        eat(TokenType.LPAREN);
        List<Object> values = parseValueList();
        eat(TokenType.RPAREN);

        eat(TokenType.SEMICOLON);

        return new InsertStmt(table, cols, values);
    }

    private SelectStmt parseSelect() {
        eat(TokenType.SELECT);
        List<String> fields = parseIdentList();

        eat(TokenType.FROM);

        String table = current.text.toLowerCase();
        eat(TokenType.IDENTIFIER);

        String whereField = null;
        Object whereValue = null;

        if (current.type == TokenType.WHERE) {
            eat(TokenType.WHERE);
            whereField = current.text.toLowerCase();
            eat(TokenType.IDENTIFIER);
            eat(TokenType.EQUAL);
            whereValue = parseValue();
        }

        eat(TokenType.SEMICOLON);

        return new SelectStmt(fields, table, whereField, whereValue);
    }

    private UpdateStmt parseUpdate() {
        eat(TokenType.UPDATE);

        String table = current.text.toLowerCase();
        eat(TokenType.IDENTIFIER);

        eat(TokenType.SET);

        Map<String,Object> assigns = new LinkedHashMap<>();
        assigns.put(current.text.toLowerCase(), null);
        String col = current.text.toLowerCase();
        eat(TokenType.IDENTIFIER);

        eat(TokenType.EQUAL);
        assigns.put(col, parseValue());

        while (current.type == TokenType.COMMA) {
            eat(TokenType.COMMA);
            col = current.text.toLowerCase();
            eat(TokenType.IDENTIFIER);
            eat(TokenType.EQUAL);
            assigns.put(col, parseValue());
        }

        String whereField = null;
        Object whereValue = null;

        if (current.type == TokenType.WHERE) {
            eat(TokenType.WHERE);
            whereField = current.text.toLowerCase();
            eat(TokenType.IDENTIFIER);
            eat(TokenType.EQUAL);
            whereValue = parseValue();
        }

        eat(TokenType.SEMICOLON);

        return new UpdateStmt(table, assigns, whereField, whereValue);
    }

    private DeleteStmt parseDelete() {
        eat(TokenType.DELETE);
        eat(TokenType.FROM);

        String table = current.text.toLowerCase();
        eat(TokenType.IDENTIFIER);

        eat(TokenType.WHERE);

        String field = current.text.toLowerCase();
        eat(TokenType.IDENTIFIER);

        eat(TokenType.EQUAL);

        Object value = parseValue();

        eat(TokenType.SEMICOLON);

        return new DeleteStmt(table, field, value);
    }

    private List<String> parseIdentList() {
        List<String> list = new ArrayList<>();
        list.add(current.text.toLowerCase());
        eat(TokenType.IDENTIFIER);

        while (current.type == TokenType.COMMA) {
            eat(TokenType.COMMA);
            list.add(current.text.toLowerCase());
            eat(TokenType.IDENTIFIER);
        }

        return list;
    }

    private List<Object> parseValueList() {
        List<Object> list = new ArrayList<>();
        list.add(parseValue());

        while (current.type == TokenType.COMMA) {
            eat(TokenType.COMMA);
            list.add(parseValue());
        }

        return list;
    }

    private Object parseValue() {
        return switch (current.type) {
            case NUMBER -> {
                int v = Integer.parseInt(current.text);
                eat(TokenType.NUMBER);
                yield v;
            }
            case STRING -> {
                String s = current.text;
                eat(TokenType.STRING);
                yield s;
            }
            default -> throw new RuntimeException("Valor inesperado: " + current);
        };
    }
}
