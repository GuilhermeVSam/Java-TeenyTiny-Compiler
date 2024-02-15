import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;

public class Main {
    public static void main(String[] args) {
        String source = "IF+-123 foo*THEN/";

        Lexer lex = new Lexer(source);

        Token token = lex.getToken();
        while(token.tokenKind != TokenType.EOF){
            System.out.println(token.tokenKind);
            token = lex.getToken();
        }
    }
}