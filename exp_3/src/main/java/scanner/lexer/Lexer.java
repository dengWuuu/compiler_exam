package scanner.lexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Lexer {
    // 文件内容的字符串
    public static String file;
    // 当前读到哪个字符
    int index = 0;

    // line 表示读到第几行
    public static int line = 1;
    // 初始化 peek 为 ‘ ’
    char peek = ' ';
    // words存储的是 String 与 相应的 Word 的映射
    HashMap<String, Word> words = new HashMap<>();
    // 白名单
    Set<Character> whitePaper = new HashSet<>();

    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    // 将关键字的 String 与相应的 Tag 对应，然后new一个Word对象
    public Lexer() {
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));
        reserve(new Word("for", Tag.FOR));
        reserve(Word.True);
        reserve(Word.False);
        //添加白名单
        whitePaper.add('+');
        whitePaper.add('-');
        whitePaper.add('*');
        whitePaper.add('/');
        whitePaper.add('%');
        whitePaper.add('\\');
        whitePaper.add('<');
        whitePaper.add('>');
        whitePaper.add('!');
        whitePaper.add('&');
        whitePaper.add('|');
        whitePaper.add('=');
        whitePaper.add(';');
        whitePaper.add(',');
        whitePaper.add('.');
        whitePaper.add('[');
        whitePaper.add(']');
        whitePaper.add('(');
        whitePaper.add(')');
        whitePaper.add('{');
        whitePaper.add('}');
        whitePaper.add('\0');
    }

    // 读取字符串中的一个字符，每次读取一位，然后Index++
    public void readch() throws IOException {
        peek = file.charAt(index++);
    }

    // 读取一个字符，并判断该字符是否等于 c
    boolean readch(char c) throws IOException {
        readch();
        if (peek != c) {
            return false;
        }
        peek = ' ';
        return true;
    }

    // 扫描字符串
    public Token scan() throws Exception {
        for (; ; readch()) {
            // 如果读到的字符串是空格，就继续读下一个
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') {
                // 如果是换行符，就行数加1
                line += 1;
            } else {
                // 否则跳出循环
                break;
            }
        }
        switch (peek) {
            case '&':
                // 判断下一个字符是否是'&'，是的话就对应一个关键词“&&”
                if (readch('&'))
                    return Word.and;
                else
                    return new Token('&');
            case '|':
                // 判断下一个字符是否是'|'，是的话就对应一个关键词“||”
                if (readch('|'))
                    return Word.or;
                else
                    return new Token('|');
            case '=':
                // 判断下一个字符是否是'='，是的话就对应一个关键词“==”
                if (readch('='))
                    return Word.eq;
                else
                    return new Token('=');
            case '!':
                // 判断下一个字符是否是'='，是的话就对应一个关键词“!=”
                if (readch('='))
                    return Word.ne;
                else
                    return new Token('!');
            case '<':
                // 判断下一个字符是否是'='，是的话就对应一个关键词"<=
                if (readch('='))
                    return Word.le;
                else
                    return new Token('<');
            case '>':
                // 判断下一个字符是否是'='，是的话就对应一个关键词">="
                if (readch('='))
                    return Word.ge;
                else
                    return new Token('>');
                // 如果读到是 '/'
            case '/':
                // 再读取一个字符
                readch();
                // 如果是'/'，那就是注释，但是随着换行符而结束
                if (getPeek() == '/') {
                    // 一直读，直到遇到换行符
                    while (true) {
                        readch();
                        if (peek == '\r' || peek == '\n') {
                            return scan();
                        }
                    }
                    // 如果是'*'，就是多行注释
                } else if (getPeek() == '*') {
                    // buf存储的是当前读取的字符的前一个字符
                    char buf = ' ';
                    while (true) {
                        readch();
                        // 当前一个字符为'*'，当前字符为'/'时，说明该多行注释结束，否则就继续往下读
                        if (buf == '*' && getPeek() == '/') {
                            readch();
                            return scan();
                        }
                        buf = getPeek();
                    }
                    // 都不是的话，就返回一个新的Token('/)
                } else return new Token('/');

        }
        // 如果是数字的话
        if (Character.isDigit(peek)) {
            int v = 0;
            // 读取一个完整的整数部分
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch();
            } while (Character.isDigit(peek));
            // 如果没有遇到‘.’，就返回一个新的整数的Token
            if (peek != '.')
                return new Num(v);
            // 否则读取浮点数后面
            float x = v;
            float d = 10;
            for (; ; ) {
                readch();
                if (!Character.isDigit(peek))
                    break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            // 整数与小数一起运算后返回
            return new Real(x);
        }
        // 如果是字母
        if (Character.isLetter(peek)) {
            // 读取完整一个变量
            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            // 如果这个变量在words中出现过，说明这个变量要么已经声明，要么是个关键词
            Word w = words.get(s);
            // 返回该关变量
            if (w != null)
                return w;
            // 否则new一个新的变量
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        if (whitePaper.contains(peek)) {
            Token tok = new Token(peek);
            peek = ' ';
            return tok;
        } else {
            throw new Exception("syntax error");
        }
    }

    private void error(String syntax_error) {
    }

    // 打印Map的size
    public void out() {
        System.out.println(words.size());

    }

    // 获取最近读到的字符
    public char getPeek() {
        return peek;
    }

    // 设置最近读到的字符
    public void setPeek(char peek) {
        this.peek = peek;
    }

}
