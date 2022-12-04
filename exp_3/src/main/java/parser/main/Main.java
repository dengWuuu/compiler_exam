package parser.main;

import parser.lexer.Lexer;
import parser.parser.Parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("exp_3/src/main/java/parser/input.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder fil = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            fil.append(line);
            fil.append('\n');
        }
        fil.delete(fil.length() - 1, fil.length());
        fil.append('\0');
        Lexer.file = fil.toString();
        reader.close();
        isr.close();
        fis.close();
        // TODO Auto-generated method stub
        Lexer lex = new Lexer();
        Parser parser = new Parser(lex);
        parser.program();
        System.out.print("\n");
    }

}
