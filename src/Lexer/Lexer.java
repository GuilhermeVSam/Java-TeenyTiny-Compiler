package Lexer;
import API.StringSlicer.StringSlicer;

import java.util.Objects;

public class Lexer {
    public String source;
    public char curChar;
    public int curPos;
    public Lexer(String source){
        this.source = source + "\n";
        this.curChar = ' ';
        this.curPos = -1;
        this.nextChar();
    }

    public void nextChar(){
        this.curPos += 1;
        if (this.curPos >= this.source.length()){
            this.curChar = '\0';
        } else{
            this.curChar = this.source.charAt(curPos);
        }
    }

    public char peek(){
        if (this.curPos + 1 >= this.source.length()) return '\0';
        return this.source.charAt(this.curPos+1);
    }

    public void abort(String message){
        System.out.println("Lexing error. " + message);
        System.exit(0);
    }

    public void skipWhitespace(){
        while(this.curChar == ' ' || this.curChar == '\t' || this.curChar == '\r'){
            this.nextChar();
        }
    }

    public void skipComment(){
        if(curChar == '#'){
            while(curChar != '\n') nextChar();
        }
    }

    public Token getToken(){
        skipWhitespace();
        skipComment();
        Token token = null;
        switch (this.curChar) {
            case '+' -> token = new Token(this.curChar, TokenType.PLUS);
            case '-' -> token = new Token(this.curChar, TokenType.MINUS);
            case '*' -> token = new Token(this.curChar, TokenType.ASTHERISK);
            case '/' -> token = new Token(this.curChar, TokenType.SLASH);
            case '\n' -> token = new Token(this.curChar, TokenType.NEWLINE);
            case '\0' -> token = new Token(this.curChar, TokenType.EOF);
            case '=' -> {
                if (peek() == '=') {
                    char lastChar = this.curChar;
                    nextChar();
                    token = new Token((char)(lastChar + this.curChar), TokenType.EQEQ);
                } else token = new Token(this.curChar, TokenType.EQ);
            }
            case '>' -> {
                if (peek() == '=') {
                    char lastChar = this.curChar;
                    nextChar();
                    token = new Token((char) (lastChar + this.curChar), TokenType.GTEQ);
                } else token = new Token(this.curChar, TokenType.GT);
            }
            case '<' ->{
                if(peek() == '='){
                    char lastChar = this.curChar;
                    nextChar();
                    token = new Token((char) (lastChar + this.curChar), TokenType.LTEQ);
                } else token = new Token(this.curChar, TokenType.LT);
            }
            case '!' -> {
                if(peek() == '=') {
                    char lastChar = this.curChar;
                    nextChar();
                    token = new Token((char) (lastChar + this.curChar), TokenType.NOTEQ);
                } else abort("Expected !=, got !" + peek());
            }
            case '\"' -> {
                nextChar();
                int startPos = this.curPos;

                while(this.curChar != '\"'){
                    if(this.curChar == '\r' || this.curChar == '\n' || this.curChar == '\t' || this.curChar == '\\' || this.curChar == '%'){
                        abort("Illegal character in string.");
                    }
                    nextChar();
                }

                String tokText = StringSlicer.sliceRange(this.source, startPos, this.curPos);
                token = new Token(tokText, TokenType.STRING);
            }
            default -> {
                if(Character.isDigit(curChar)){
                    int startPos = curPos;
                    while(Character.isDigit(this.peek())){
                        nextChar();
                    }
                    if(this.peek() == '.'){
                        nextChar();
                        if(!Character.isDigit(peek())){
                            abort("Illegal character in number.");
                        }
                        while(Character.isDigit(peek())){
                            nextChar();
                        }
                    }
                    String tokText = StringSlicer.sliceRange(source, startPos, this.curPos + 1);
                    token = new Token(tokText, TokenType.NUMBER);
                } else if (Character.isAlphabetic(curChar)){
                    int startPos = this.curPos;
                    while(Character.isLetterOrDigit(peek())){
                        nextChar();
                    }
                    String tokText = StringSlicer.sliceRange(source, startPos, this.curPos +1);
                    TokenType keyword = Token.checkIfKeyword(tokText);
                    if(keyword == null) token = new Token(tokText, TokenType.IDENT);
                    else token = new Token(tokText, keyword);
                }
                else abort("Unknown token: " + this.curChar);
            }
        }
        this.nextChar();
        return token;
    }
}
