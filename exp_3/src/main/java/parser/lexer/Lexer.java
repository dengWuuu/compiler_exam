package parser.lexer;

import java.io.IOException;
import java.util.Hashtable;

import parser.lexer.Num;
import parser.lexer.Token;
import parser.lexer.Word;
import parser.symbols.Type;

/*
 * reads characters from the input and groups them into "Token" object
 * */
public class Lexer {

    public static String file; // 读取文件
    int index = 0; // 当前读到第几个字符

    public static int line = 1; // 静态变量用于记录代码行号
    char peek = ' '; //获取输入字符
    Hashtable words = new Hashtable();

    // Hashtable put(key, value)
    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    // 构造函数，预存保留字
    public Lexer() {
        // 条件、循环
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));
        // dcris
        reserve(new Word("for", Tag.FOR));
        // 布尔值
        reserve(Word.True);
        reserve(Word.False);
        // Basic type基本数据类型
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);
    }

    void readch() throws IOException { //读入decaf源码的字符
//        peek = (char) System.in.read();
        peek = file.charAt(index++);

    }

    boolean readch(char c) throws IOException { //判定符号c是否与下一个读入符号匹配
        readch();
        if (peek != c) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for (; ; readch()) { //循环读入decaf源码的空白字符, 忽略
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line = line + 1; // 如果换行，line+1
            else break; //其它符号循环终止
        }
        switch (peek) { // 如果是逻辑和关系运算符号，输出该token，否则创建新token（报错）
            case '&':
                if (readch('&')) return Word.and;
                else return new Token('&'); // ‘&’自动转换为ASCII值
            case '|':
                if (readch('|')) return Word.or;
                else return new Token('|');
            case '=':
                if (readch('=')) return Word.eq;
                else return new Token('=');
            case '!':
                if (readch('=')) return Word.ne;
                else return new Token('!');
            case '<':
                if (readch('=')) return Word.le;
                else return new Token('<');
            case '>':
                if (readch('=')) return Word.ge;
                else return new Token('>');
            // 如果读到是 '/'
            case '/':
                // 再读取一个字符
                readch();
                // 如果是'/'，那就是注释，但是随着换行符而结束
                if (peek == '/') {
                    // 一直读，直到遇到换行符
                    while (true){
                        readch();
                        if (peek == '\r' || peek == '\n'){
                            return scan();
                        }
                    }
                // 如果是'*'，就是多行注释
                }else if (peek == '*'){
                    // buf存储的是当前读取的字符的前一个字符
                    char buf = ' ';
                    while (true){
                        readch();
                        // 当前一个字符为'*'，当前字符为'/'时，说明该多行注释结束，否则就继续往下读
                        if (buf == '*' && peek == '/'){
                            readch();
                            return scan();
                        }
                        buf = peek;
                    }
                // 都不是的话，就返回一个新的Token('/)
                }else return new Token('/');
        }

        if (Character.isDigit(peek)) { //如果peek是数字
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch(); //Character.digit(char ch, int radix)
            } while (Character.isDigit(peek));
            if (peek != '.') return new Num(v); //是不是小数点，如果是则 抽取float，否则 输出int token（Tag.NUM）
            float x = v;
            float d = 10;
            for (; ; ) {
                readch();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            return new Real(x); // 输出float token(Tag.REAL)
        }

        if (Character.isLetter(peek)) { //如果peek是字母
            StringBuffer b = new StringBuffer(); //b需要多次修改(append)，使用 StringBuffer
            do {
                b.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = (Word) words.get(s); //串是否为预存的保留字（if else do while break）
            if (w != null) return w; // 如果是保留字，输出
            w = new Word(s, Tag.ID); //如果不是预存的保留字，则构建Word，Tag.ID
            words.put(s, w); //存入hashtable
            return w; //输出ID串
        }

        Token tok = new Token(peek);
        peek = ' ';
        return tok; //输出其它符号 +， - ，*， /，！，>, <, =, &, |
    }
}
