package lexer;

public class Token {
    public final TokenType type;
    public final String text;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String toString() {
        return type + "(" + text + ")";
    }
}
