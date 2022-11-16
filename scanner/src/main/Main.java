package main;

import java.io.IOException;
import java.util.Hashtable;

import lexer.Lexer;
import lexer.Token;

public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        char c;
        do {
            Token token = lexer.scan();
            switch (token.tag) {
                case 270:
                case 272:
                    System.out.println("(NUM , " + token + ")");
                    break;
                case 264:
                    System.out.println("(ID , " + token + ")");
                    break;
                case 256:
                case 257:
                case 258:
                case 259:
                case 260:
                case 265:
                case 274:
                case 275:
                case 276:
                    System.out.println("(KEY , " + token + ")");
                    break;
                case 13:
                    break;
                default:
                    System.out.println("(SYM , " + token + ")");
                    break;
            }

        } while (lexer.getPeek() != '\n');
    }
}