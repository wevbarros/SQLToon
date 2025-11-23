package lexer;

public class Lexer {

    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    private char current() {
        return pos < input.length() ? input.charAt(pos) : '\0';
    }

    private void advance() {
        pos++;
    }

    public Token next() {

        while (Character.isWhitespace(current())) advance();
        char c = current();

        if (c == '\0') return new Token(TokenType.EOF, "");

        switch (c) {
            case '(' -> { advance(); return new Token(TokenType.LPAREN, "("); }
            case ')' -> { advance(); return new Token(TokenType.RPAREN, ")"); }
            case ',' -> { advance(); return new Token(TokenType.COMMA, ","); }
            case ';' -> { advance(); return new Token(TokenType.SEMICOLON, ";"); }
            case '=' -> { advance(); return new Token(TokenType.EQUAL, "="); }
        }

        if (c == '"') {
            advance();
            int start = pos;
            while (current() != '"' && current() != '\0') advance();
            String s = input.substring(start, pos);
            advance();
            return new Token(TokenType.STRING, s);
        }

        if (Character.isDigit(c)) {
            int start = pos;
            while (Character.isDigit(current())) advance();
            return new Token(TokenType.NUMBER, input.substring(start, pos));
        }

        if (Character.isLetter(c)) {
            int start = pos;
            while (Character.isLetterOrDigit(current()) || current() == '_') advance();

            String w = input.substring(start, pos).toUpperCase();

            return switch (w) {
                case "SELECT" -> new Token(TokenType.SELECT, w);
                case "INSERT" -> new Token(TokenType.INSERT, w);
                case "INTO" -> new Token(TokenType.INTO, w);
                case "VALUES" -> new Token(TokenType.VALUES, w);
                case "UPDATE" -> new Token(TokenType.UPDATE, w);
                case "SET" -> new Token(TokenType.SET, w);
                case "DELETE" -> new Token(TokenType.DELETE, w);
                case "FROM" -> new Token(TokenType.FROM, w);
                case "WHERE" -> new Token(TokenType.WHERE, w);
                default -> new Token(TokenType.IDENTIFIER, w);
            };
        }

        throw new RuntimeException("Unexpected character: " + c);
    }
}
