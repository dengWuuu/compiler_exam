package lexer;

import java.io.IOException;
import java.util.Hashtable;

import symbols.Type;

/*
 * reads characters from the input and groups them into "Token" object
 * */
public class Lexer {
    public static int line = 1; // 静态变量用于记录代码行号
    char peek = ' '; //获取输入字符
    Hashtable words = new Hashtable();

    void reserve(Word w) {
        words.put(w.lexeme, w);
    } // Hashtable put(key, value)

    public Lexer() { // 构造函数，预存保留字
        // 条件、循环
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));
        // 布尔值
        reserve(Word.True);
        reserve(Word.False);
        // Basic type基本数据类型
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);
    }

    //读入decaf源码的字符
    void readch() throws IOException {
        peek = (char) System.in.read();
    }

    //判定符号c是否与下一个读入符号匹配
    boolean readch(char c) throws IOException {
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
        }

        if (Character.isDigit(peek)) { //如果peek是数字
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch();
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
