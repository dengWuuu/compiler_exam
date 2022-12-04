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

    public static String file; // ��ȡ�ļ�
    int index = 0; // ��ǰ�����ڼ����ַ�

    public static int line = 1; // ��̬�������ڼ�¼�����к�
    char peek = ' '; //��ȡ�����ַ�
    Hashtable words = new Hashtable();

    // Hashtable put(key, value)
    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    // ���캯����Ԥ�汣����
    public Lexer() {
        // ������ѭ��
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));
        // dcris
        reserve(new Word("for", Tag.FOR));
        // ����ֵ
        reserve(Word.True);
        reserve(Word.False);
        // Basic type������������
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);
    }

    void readch() throws IOException { //����decafԴ����ַ�
//        peek = (char) System.in.read();
        peek = file.charAt(index++);

    }

    boolean readch(char c) throws IOException { //�ж�����c�Ƿ�����һ���������ƥ��
        readch();
        if (peek != c) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for (; ; readch()) { //ѭ������decafԴ��Ŀհ��ַ�, ����
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line = line + 1; // ������У�line+1
            else break; //��������ѭ����ֹ
        }
        switch (peek) { // ������߼��͹�ϵ������ţ������token�����򴴽���token������
            case '&':
                if (readch('&')) return Word.and;
                else return new Token('&'); // ��&���Զ�ת��ΪASCIIֵ
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
            // ��������� '/'
            case '/':
                // �ٶ�ȡһ���ַ�
                readch();
                // �����'/'���Ǿ���ע�ͣ��������Ż��з�������
                if (peek == '/') {
                    // һֱ����ֱ���������з�
                    while (true){
                        readch();
                        if (peek == '\r' || peek == '\n'){
                            return scan();
                        }
                    }
                // �����'*'�����Ƕ���ע��
                }else if (peek == '*'){
                    // buf�洢���ǵ�ǰ��ȡ���ַ���ǰһ���ַ�
                    char buf = ' ';
                    while (true){
                        readch();
                        // ��ǰһ���ַ�Ϊ'*'����ǰ�ַ�Ϊ'/'ʱ��˵���ö���ע�ͽ���������ͼ������¶�
                        if (buf == '*' && peek == '/'){
                            readch();
                            return scan();
                        }
                        buf = peek;
                    }
                // �����ǵĻ����ͷ���һ���µ�Token('/)
                }else return new Token('/');
        }

        if (Character.isDigit(peek)) { //���peek������
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch(); //Character.digit(char ch, int radix)
            } while (Character.isDigit(peek));
            if (peek != '.') return new Num(v); //�ǲ���С���㣬������� ��ȡfloat������ ���int token��Tag.NUM��
            float x = v;
            float d = 10;
            for (; ; ) {
                readch();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            return new Real(x); // ���float token(Tag.REAL)
        }

        if (Character.isLetter(peek)) { //���peek����ĸ
            StringBuffer b = new StringBuffer(); //b��Ҫ����޸�(append)��ʹ�� StringBuffer
            do {
                b.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = (Word) words.get(s); //���Ƿ�ΪԤ��ı����֣�if else do while break��
            if (w != null) return w; // ����Ǳ����֣����
            w = new Word(s, Tag.ID); //�������Ԥ��ı����֣��򹹽�Word��Tag.ID
            words.put(s, w); //����hashtable
            return w; //���ID��
        }

        Token tok = new Token(peek);
        peek = ' ';
        return tok; //����������� +�� - ��*�� /������>, <, =, &, |
    }
}
